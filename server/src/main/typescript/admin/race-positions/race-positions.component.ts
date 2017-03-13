import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormControl, FormGroup, FormArray } from '@angular/forms';
import { Validators, AbstractControl, ValidatorFn } from '@angular/forms';
import { ActivatedRoute, Params } from '@angular/router';

import { Race } from '../../races/race.model';
import { Position } from '../../positions/position.model';

import { RacePositionTemplate } from './race-position-template.model';
import { RacePositionsService } from './race-positions.service';
import { AdminRacesService } from '../races/races.service';
import { AdminPositionsService } from '../positions/positions.service';

@Component({
  selector: 'race-positions',
  styleUrls: [ './race-positions.component.css' ],
  templateUrl: './race-positions.component.html'
})
export class RacePositionsComponent implements OnInit, OnDestroy {
  public templates: RacePositionTemplate[] = new Array<RacePositionTemplate>();
  public templatesForRace: RacePositionTemplate[] = new Array<RacePositionTemplate>();
  public addPositionForm: FormGroup;
  public selectedRace: Race;
  public races: Race[] = new Array<Race>();
  public positions: Position[] = new Array<Position>();

  private raceId: number;
  private templatesSubscription: any;
  private racesSubscription: any;
  private positionsSubscription: any;

  constructor(
    private templateService: RacePositionsService,
    private racesService: AdminRacesService,
    private positionsService: AdminPositionsService,
    private route: ActivatedRoute
  ) {}

  public ngOnInit() {
    this.addPositionForm = new FormGroup({
      name: this.getNameControlWithValidators(''),
      defaultTemplate: new FormControl(false, [ this.validateNotExistingDefault() ])
    });
    this.racesSubscription = this.racesService.getRaces()
      .subscribe((races: Race[]) => {
        this.races = races;
        this.selectedRace = this.races[0];
        if (this.raceId) {
          this.selectedRace = this.getRaceForId(this.raceId);
        }
        this.changedRace(this.selectedRace);
    });
    this.positionsSubscription = this.positionsService.getPositions()
      .subscribe((positions: Position[]) => {
        this.positions = positions;
    });
    this.templatesSubscription = this.templateService.getTemplates()
      .subscribe((templates: RacePositionTemplate[]) => {
        this.templates = templates;
        if (this.selectedRace) {
          this.templatesForRace = this.getTemplatesForRace(this.selectedRace);
        }
    });
    this.route.params.subscribe((params: Params) => {
      this.raceId = +params['raceId'];
      console.log('raceId ' + this.raceId);
      if (this.raceId && this.races.length > 0) {
        this.selectedRace = this.getRaceForId(this.raceId);
        this.changedRace(this.selectedRace);
      }
    });
  }

  public ngOnDestroy() {
    this.racesSubscription.unsubscribe();
    this.templatesSubscription.unsubscribe();
    this.positionsSubscription.unsubscribe();
  }

  public changedRace(race: Race): void {
    console.log('changed race');
    console.log(race);
    // get templates for selected race
    this.templatesForRace = this.getTemplatesForRace(race);
  }

  public onSubmit({ value, valid }: { value: RacePositionTemplate, valid: boolean }) {
    console.log(value);
    let template: RacePositionTemplate = new RacePositionTemplate();
    template.name = value.name;
    template.race = this.selectedRace.id;
    if (this.templatesForRace.length === 0) {
      template.defaultTemplate = true;
    } else {
      template.defaultTemplate = value.defaultTemplate;
    }
    this.templateService.addTemplate(template);
    this.addPositionForm.reset({
      name: ''
    });
  }

  public removeTemplate(template: RacePositionTemplate): void {
    this.templateService.removeTemplate(template);
  }

  private getRaceForId(id: number): Race {
    for (let race of this.races) {
      if (race.id === id) {
        return race;
      }
    }
    return null;
  }

  private getTemplatesForRace(race: Race): RacePositionTemplate[] {
    return this.templates
      .filter((template: RacePositionTemplate) => template.race === race.id);
  }

  private getNameControlWithValidators(value: string): FormControl {
    return new FormControl(value, [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(250),
        this.validateRacePositionName()
      ]);
  }

  private validateNotExistingDefault(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.selectedRace && this.templatesForRace) {
        for (let template of this.templatesForRace) {
          if (template.defaultTemplate === control.value) {
            console.log('existing default');
            console.log(this.templatesForRace);
            return { existingDefault: true };
          }
        }
      }

      return null;
    };
  }

  private validateRacePositionName(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.selectedRace && this.templatesForRace) {
        for (let template of this.templatesForRace) {
          if (template.name === control.value) {
            console.log('selected race ' + this.selectedRace.id);
            console.log('template ' + template.id);
            return { existingName: true };
          }
        }
      }

      return null;
    };
  }
}
