import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormArray } from '@angular/forms';
import { Validators, AbstractControl, ValidatorFn } from '@angular/forms';

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
export class RacePositionsComponent implements OnInit {
  public templates: RacePositionTemplate[] = new Array<RacePositionTemplate>();
  public templatesForRace: RacePositionTemplate[] = new Array<RacePositionTemplate>();
  public addPositionForm: FormGroup;
  public selectedRace: Race;
  public races: Race[] = new Array<Race>();
  public positions: Position[] = new Array<Position>();

  constructor(
    private templateService: RacePositionsService,
    private racesService: AdminRacesService,
    private positionsService: AdminPositionsService
  ) {}

  public ngOnInit() {
    this.addPositionForm = new FormGroup({
      name: this.getNameControlWithValidators(''),
      defaultTemplate: new FormControl(false, [ this.validateNotExistingDefault() ])
    });
    this.racesService.getRaces()
      .subscribe((races: Race[]) => {
        this.races = races;
        if (!this.selectedRace && this.races.length > 0) {
          this.selectedRace = this.races[0];
          this.changedRace(this.selectedRace);
        }
    });
    this.positionsService.getPositions()
      .subscribe((positions: Position[]) => {
        this.positions = positions;
    });
    this.templateService.getTemplates()
      .subscribe((templates: RacePositionTemplate[]) => {
        this.templates = templates;
        if (this.selectedRace) {
          this.templatesForRace = this.getTemplatesForRace(this.selectedRace);
        }
    });
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
      if (this.selectedRace) {
        for (let template of this.templates) {
          if (template.defaultTemplate === control.value && this.selectedRace.id !== template.id) {
            return { existingDefault: true };
          }
        }
      }

      return null;
    };
  }

  private validateRacePositionName(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.selectedRace) {
        for (let template of this.templates) {
          if (template.name === control.value && this.selectedRace.id !== template.id) {
            return { existingName: true };
          }
        }
      }

      return null;
    };
  }
}
