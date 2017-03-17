import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormArray } from '@angular/forms';
import { Validators, AbstractControl, ValidatorFn } from '@angular/forms';
import { ActivatedRoute, Params, Router } from '@angular/router';

import { RacePositionTemplate } from '../race-positions/race-position-template.model';
import { Race } from '../../races/race.model';
import { Position } from '../../positions/position.model';
import { RacePositionTemplateEntry } from './race-position-template-entry.model';

import { RacePositionTemplatesService } from './race-position-templates.service';
import { RacePositionsService } from '../race-positions/race-positions.service';
import { AdminRacesService } from '../races/races.service';
import { AdminPositionsService } from '../positions/positions.service';

@Component({
  selector: 'race-position-templates',
  styleUrls: [ './race-position-templates.component.css' ],
  templateUrl: './race-position-templates.component.html'
})
export class RacePositionTemplatesComponent implements OnInit, OnDestroy {

  public addEntryForm: FormGroup;
  public positionsForm: FormGroup;
  public templates: RacePositionTemplate[] = new Array<RacePositionTemplate>();
  public template: RacePositionTemplate;
  public race: Race;
  public racePositions: Position[] = new Array<Position>();
  public unassignedPositions: Position[] = new Array<Position>();

  private templateId: number;
  private races: Race[] = new Array<Race>();

  private templatesSubscription: any;
  private templateEntriesSubscription: any;
  private racesSubscription: any;
  private positionsSubscription: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private templateEntriesService: RacePositionTemplatesService,
    private positionsService: AdminPositionsService,
    private templatesService: RacePositionsService,
    private racesService: AdminRacesService,
    private fb: FormBuilder
  ) {}

  public ngOnInit() {
    let positionsFormArray: FormArray = this.fb.array([]);
    this.positionsForm = this.fb.group({
      positions: positionsFormArray
    });

    this.addEntryForm = this.fb.group({});

    this.positionsSubscription = this.positionsService.getPositions()
      .subscribe((positions: Position[]) => {
        this.racePositions = positions;
        this.unassignedPositions = this.findUnassignedPositions();
        this.populateAddEntryPosition();
        this.populateTemplates();
    });

    this.templatesSubscription = this.templatesService.getTemplates()
      .subscribe((templates: RacePositionTemplate[]) => {
        this.templates = templates;
        this.populateTemplates();
    });

    this.racesSubscription = this.racesService.getRaces()
      .subscribe((races: Race[]) => {
        this.races = races;
        if (this.template) {
          this.race = this.getRaceForId(this.template.race);
        }
    });
    this.route.params.subscribe((params: Params) => {
        this.templateId = +params['templateId'];
        if (this.templates.length > 0) {
          this.template = this.getTemplateForId(this.templateId);
          if (this.races.length > 0) {
            this.race = this.getRaceForId(this.template.race);
          }
          this.populateTemplates();
          this.unassignedPositions = this.findUnassignedPositions();
          this.populateAddEntryPosition();
        }
    });
  }

  public ngOnDestroy() {
    this.racesSubscription.unsubscribe();
    this.positionsSubscription.unsubscribe();
    if (this.templateEntriesSubscription) {
      this.templateEntriesSubscription.unsubscribe();
    }
    this.templatesSubscription.unsubscribe();
  }

  public goBack(): void {
    this.router.navigateByUrl('/race-positions/' + this.template.race);
  }

  public updateTemplateEntry(position: number, entry: RacePositionTemplateEntry) {
    entry.position = +position;
    this.templateEntriesService.updateTemplate(entry);
  }

  public removeTemplateEntry(entry: RacePositionTemplateEntry): void {
    this.templateEntriesService.removeTemplate(entry);
  }

  public onSubmit({ value, valid }: { value: any, valid: boolean }) {
    let position: Position = value.position;
    let template: RacePositionTemplateEntry = new RacePositionTemplateEntry();
    template.position = position.id;
    template.positionOrder = 1;
    template.template = this.template.id;

    // get last positionOrder in templates
    if (this.template.templates.length > 0) {
      template.positionOrder =
        this.template.templates[this.template.templates.length - 1].positionOrder + 1;
    }

    this.templateEntriesService.addTemplate(template);

    // remove position from unassignedPositions
    let index = this.unassignedPositions.indexOf(position);
    this.unassignedPositions.splice(index, 1);
    this.addEntryForm.removeControl('position');
    this.populateAddEntryPosition();
  }

  private populateTemplates(): void {
      if (this.templateId) {
        this.template = this.getTemplateForId(this.templateId);
        if (this.races.length > 0) {
          this.race = this.getRaceForId(this.template.race);
        }
      }
      if (this.template) {
        if (this.template.templates) {
          this.templateEntriesService.setTemplates(this.template.templates);
        }
        this.templateEntriesSubscription = this.templateEntriesService.getTemplates()
          .subscribe((templateEntries: RacePositionTemplateEntry[]) => {
            this.template.templates = templateEntries;
            this.positionsForm.removeControl('positions');
            let positionsFormArray = this.fb.array([]);
            this.positionsForm = this.fb.group({
              positions: positionsFormArray
            });
            this.template.templates.map((entry: RacePositionTemplateEntry) =>
              this.addTemplateEntryToFormArray(entry, positionsFormArray));
            this.unassignedPositions = this.findUnassignedPositions();
            this.populateAddEntryPosition();
      });
    }
  }
  private populateAddEntryPosition(): void {
    if (this.unassignedPositions.length > 0) {
        let control: AbstractControl =
          this.fb.control(this.unassignedPositions[0], Validators.required);
        this.addEntryForm.addControl('position', control);
    }
  }

  private findUnassignedPositions(): Position[] {
    let unassigned: Position[] = new Array<Position>();
    if (this.template && this.template.templates && this.racePositions) {
      this.racePositions.forEach((position: Position) => {
        let found: boolean = false;
        this.template.templates.forEach((entry: RacePositionTemplateEntry) => {
          if (entry.position === position.id) {
            found = true;
          }
        });
        if (!found) {
          unassigned.push(position);
        }
      });
    }
    return unassigned;
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
    array.push(positionGroup);
  }

  private getRaceForId(id: number): Race {
    for (let race of this.races) {
      if (race.id === id) {
        return race;
      }
    }
    return null;
  }
  private getTemplateForId(id: number): RacePositionTemplate {
    for (let template of this.templates) {
      if (template.id === id) {
        return template;
      }
    }
    return null;
  }

  private checkExistingPosition(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      let position: number = +control.get('position').value as number;
      let entry: RacePositionTemplateEntry = control.get('entry').value;
      if (+entry.position === +position) {
        return { samePosition: true };
      }
      let array: FormArray =  this.positionsForm.get('positions') as FormArray;
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
      if (this.template) {
        for (let template of this.template.templates) {
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

}
