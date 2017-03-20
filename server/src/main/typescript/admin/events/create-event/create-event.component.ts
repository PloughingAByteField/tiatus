import { Component, OnInit, OnDestroy } from '@angular/core';
import { Location } from '@angular/common';
import { FormBuilder, FormControl, FormGroup, FormArray } from '@angular/forms';
import { Validators, AbstractControl, ValidatorFn } from '@angular/forms';

import { Race } from '../../../races/race.model';
import { SelectedRaceService } from '../../races/selected-race.service';
import { AdminRacesService } from '../../races/races.service';

import { Event } from '../../../events/event.model';
import { AdminEventsService } from '../events.service';

import { RacePositionTemplate } from '../../race-positions/race-position-template.model';

import { Position } from '../../../positions/position.model';
import { RacePositionTemplateEntry } from
  '../../race-position-templates/race-position-template-entry.model';

import { RacePositionTemplatesService } from
  '../../race-position-templates/race-position-templates.service';
import { RacePositionsService } from '../../race-positions/race-positions.service';

import { AdminPositionsService } from '../../positions/positions.service';

@Component({
  selector: 'create-event',
  styleUrls: [ './create-event.component.css' ],
  templateUrl: './create-event.component.html'
})
export class CreateEventComponent implements OnInit, OnDestroy {

  public addEntryForm: FormGroup;

  public selectedRace: Race = null;
  public selectedTemplate: RacePositionTemplate = null;
  public races: Race[] = new Array<Race>();
  public positions: Position[] = new Array<Position>();
  public unassignedPositions: Position[] = new Array<Position>();
  public templates: RacePositionTemplate[] = new Array<RacePositionTemplate>();

  private events: Event[];
  private unassignedEvents: Event[];

  private racesSubscription: any;
  private selectedRaceSubscription: any;
  private templatesSubscription: any;
  private positionsSubscription: any;
  private eventsSubscription: any;

  constructor(
    private selectedRaceService: SelectedRaceService,
    private racesService: AdminRacesService,
    private positionsService: AdminPositionsService,
    private templatesService: RacePositionsService,
    private eventsService: AdminEventsService,
    private fb: FormBuilder,
    private location: Location
  ) {}

  public ngOnInit() {
    let positionsFormArray: FormArray = this.fb.array([]);
    this.addEntryForm = this.fb.group({
      name: this.fb.control('', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(250),
        this.existingEventName()
      ]),
      isNonRaceAssigned: this.fb.control(false),
      newPosition: this.fb.control(''),
      positions: positionsFormArray
    });

    this.selectedRaceSubscription =
      this.selectedRaceService.getSelectedRace.subscribe((race: Race) => {
        console.log(race);
        if (race !== null) {
          this.selectedRace = race;
          this.changedRace(this.selectedRace);
        }
    });

    this.racesSubscription = this.racesService.getRaces()
      .subscribe((races: Race[]) => {
        this.races = races;
        if (races.length > 0 && this.selectedRaceService.getSelectedRace.getValue() === null) {
          this.selectedRace = this.races[0];
          this.changedRace(this.selectedRace);
        }
    });

    this.positionsSubscription = this.positionsService.getPositions()
      .subscribe((positions: Position[]) => {
        console.log('psoitions');
        this.positions = positions;
        this.fillUnassignedPositions();
        console.log(this.unassignedPositions);
        console.log(this.positions);
    });

    this.eventsSubscription = this.eventsService.getEvents()
      .subscribe((events: Event[]) => this.events = events);
  }

  public ngOnDestroy() {
    this.selectedRaceSubscription.unsubscribe();
    this.racesSubscription.unsubscribe();
    this.positionsSubscription.unsubscribe();
    this.eventsSubscription.unsubscribe();
  }

  public onSubmit({ value, valid }: { value: any, valid: boolean }) {
    console.log('on submit');
  }

  public changeRace(race: Race): void {
    console.log('change race by ui');
    console.log(race);
    this.changedRace(race);
  }

  public changeTemplate(template: RacePositionTemplate): void {
    console.log('change template by ui');
    console.log(template);
    this.changedTemplate(template);
  }

  public removePosition(index: number): void {
    let array: FormArray = this.addEntryForm.get('positions') as FormArray;
    for (let i = index + 1; i < array.controls.length; i++) {
      let group: FormGroup = array.controls[i] as FormGroup;
      let orderValue: number = group.get('positionOrder').value;
      let orderControl: AbstractControl = group.get('positionOrder');
      orderControl.setValue(orderValue - 1);
    }
    let group: FormGroup = array.controls[index] as FormGroup;
    array.removeAt(index);
    group.removeControl('position');
    group.removeControl('positionOrder');
    group.removeControl('entry');
    this.fillUnassignedPositions();
  }

  public addPosition(): void {
    let newPosition = this.addEntryForm.get('newPosition').value;
    let array: FormArray = this.addEntryForm.get('positions') as FormArray;
    let entry: RacePositionTemplateEntry = new RacePositionTemplateEntry();
    entry.position = newPosition.id;
    entry.positionOrder = 1;
    if (array.controls.length > 0) {
      let lastPositionOrder = array.controls[array.controls.length - 1].get('positionOrder').value;
      entry.positionOrder = lastPositionOrder + 1;
    }
    this.addTemplateEntryToFormArray(entry, array);
    // remove from unassigned
    this.unassignedPositions.splice(this.unassignedPositions.indexOf(newPosition), 1);
    this.fillUnassignedPositions();
  }

  public hasPositionChanged(entry: RacePositionTemplateEntry, position: number): boolean {
    if (+entry.position === +position) {
      return false;
    } else {
      return true;
    }
  }

  public goBack(): void {
    this.location.back();
  }

  private fillUnassignedPositions(): void {
    this.unassignedPositions = this.positions.filter((position: Position) =>
          !this.positionInForm(position));
    if (this.unassignedPositions.length > 0) {
      this.addEntryForm.setControl('newPosition', this.fb.control(this.unassignedPositions[0]));
    }
  }

  private changedRace(race: Race): void {
    console.log('changed race');
    console.log(race);
    this.selectedRace = race;
    this.templates = this.templatesService.getTemplatesForRace(race);
    this.processTemplates(this.templates);
    console.log(this.templates);
    this.changedTemplate(this.selectedTemplate);
  }

  private processTemplates(templates: RacePositionTemplate[]): void {
    if (templates.length > 0) {
      let found: boolean = false;
      for (let template of templates) {
        if (template.defaultTemplate) {
          this.changedTemplate(template);
          found = true;
          break;
        }
      }
      if (!found) {
        // take first if we have no default
        this.selectedTemplate = templates[0];
      }
    } else {
      // if we have no templates reset selected template to null
      this.selectedTemplate = null;
    }
  }

  private changedTemplate(template: RacePositionTemplate): void {
    console.log('changed template');
    console.log(template);
    this.selectedTemplate = template;
    this.populateFormPositions(template);
    this.fillUnassignedPositions();
  }

  private positionInForm(position: Position): boolean {
    let array: FormArray = this.addEntryForm.get('positions') as FormArray;
    for (let control of array.controls) {
      let entry: RacePositionTemplateEntry = control.get('entry').value;
      if (position.id === +entry.position) {
        return true;
      }
    }
    return false;
  }

  private populateFormPositions(template: RacePositionTemplate): void {
      this.addEntryForm.removeControl('positions');
      let positionsFormArray = this.fb.array([]);
      this.addEntryForm.setControl('positions', positionsFormArray);
      if (template) {
        template.templates.map((entry: RacePositionTemplateEntry) =>
          this.addTemplateEntryToFormArray(entry, positionsFormArray));
    }
  }

  private addTemplateEntryToFormArray(
      entry: RacePositionTemplateEntry, array: FormArray): void {
    let positionControl: FormControl =
      this.fb.control(entry.position);
    let positionOrderControl: FormControl = this.fb.control(entry.positionOrder);
    let entryControl: FormControl = this.fb.control(entry);
    let positionGroup = this.fb.group({
      position: positionControl,
      positionOrder: positionOrderControl,
      entry: entryControl
    }, {
      validator: this.checkExistingPosition()
    });
    // positionGroup.addControl('position_' + entry.positionOrder, positionControl);
    array.push(positionGroup);
  }

  private checkExistingPosition(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      let position: number;
      if (control.get('position')) {
        position = +control.get('position').value as number;
      }
      let positionOrder: number;
      if (control.get('positionOrder')) {
        positionOrder = +control.get('positionOrder').value as number;
      }
      let entry: RacePositionTemplateEntry;
      if (control.get('entry')) {
        entry = control.get('entry').value;
      }
      if (entry) {
        if (+entry.position === +position && +entry.positionOrder !== +positionOrder) {
          return { samePosition: true };
        }
      }
      let array: FormArray =  this.addEntryForm.get('positions') as FormArray;
      for (let group of array.controls) {
        if (control !== group) {
          let groupPositon: number = +group.get('position').value as number;
          if (position === groupPositon) {
            return { existingPosition: true };
          }
        }
      }
      // now check to see if matching against the backing
      // templates when 2 positions have been swapped
      if (this.selectedTemplate && entry) {
        for (let template of this.selectedTemplate.templates) {
          if (entry.position !== template.position) {
            if (+template.position === +position) {
              return { existingPosition: true };
            }
          }
        }
      }
      return null;
    };
  }

  private existingEventName(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      // check to see if check box is ticked if so check unassigned else check assigned for race
      return null;
    };
  }
}
