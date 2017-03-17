import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormArray } from '@angular/forms';
import { Validators, AbstractControl, ValidatorFn } from '@angular/forms';

import { Race } from '../../../races/race.model';
import { SelectedRaceService } from '../../races/selected-race.service';
import { AdminRacesService } from '../../races/races.service';

import { RacePositionTemplate } from '../../race-positions/race-position-template.model';

import { Position } from '../../../positions/position.model';
import { RacePositionTemplateEntry } from
  '../race-position-templates/race-position-template-entry.model';

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
  public positionsForm: FormGroup;

  public selectedRace: Race = null;
  public selectedTemplate: RacePositionTemplate = null;
  public races: Race[] = new Array<Race>();
  public positions: Position[] = new Array<Position>();
  public templates: RacePositionTemplate[] = new Array<RacePositionTemplate>();

  private racesSubscription: any;
  private selectedRaceSubscription: any;
  private templatesSubscription: any;
  private positionsSubscription: any;

  constructor(
    private selectedRaceService: SelectedRaceService,
    private racesService: AdminRacesService,
    private positionsService: AdminPositionsService,
    private templatesService: RacePositionsService,
    private fb: FormBuilder
  ) {}

  public ngOnInit() {
    let positionsFormArray: FormArray = this.fb.array([]);
    this.positionsForm = this.fb.group({
      positions: positionsFormArray
    });

    this.addEntryForm = this.fb.group({});

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
        this.positions = positions;
    });

  }

  public ngOnDestroy() {
    this.selectedRaceSubscription.unsubscribe();
    this.racesSubscription.unsubscribe();
    this.positionsSubscription.unsubscribe();
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

  private changedRace(race: Race): void {
    console.log('changed race');
    console.log(race);
    this.selectedRace = race;
    this.templates = this.templatesService.getTemplatesForRace(race);
    this.processTemplates(this.templates);
    console.log(this.templates);
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
  }
}
