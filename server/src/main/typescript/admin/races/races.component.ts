import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators, AbstractControl, ValidatorFn } from '@angular/forms';

import { Race } from '../../races/race.model';
import { AdminRacesService } from './races.service';
import { ExistingRaceNameValidator } from './ExistingRaceNameValidator';

@Component({
  selector: 'races',
  styleUrls: [ './races.component.css' ],
  templateUrl: './races.component.html'
})
export class RacesComponent implements OnInit {
  public raceForm: FormGroup;

  public races: Race[] = new Array<Race>();

  constructor(private racesService: AdminRacesService) {}

  public ngOnInit() {
    this.raceForm = new FormGroup({
      name: new FormControl('', [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(10),
          this.validateRaceName()
      ]),
      raceOrder: new FormControl(this.races.length + 1, [
          Validators.required, this.validateRaceOrder()
      ])
    });
    this.racesService.getRaces()
      .subscribe((races: Race[]) => {
        console.log(races);
        this.races = races;
        this.raceForm.patchValue({
          raceOrder: this.races.length + 1
        });
      });
  }

  public onSubmit({ value, valid }: { value: Race, valid: boolean }) {
    console.log(value);
    console.log(valid);
    let race: Race = new Race();
    race.name = value.name;
    race.raceOrder = value.raceOrder;
    this.racesService.addRace(race);
    this.raceForm.reset({
      name: '',
      raceOrder: race.raceOrder + 1
    });
  }

  private isEmptyInputValue(value: any): boolean {
    return value == null || value.length === 0;
  }

  private validateRaceName(): ValidatorFn {
    return (control: AbstractControl): {[key: string]: any} => {
      for (let race of this.races) {
        if (race.name === control.value) {
          return { existingName: true };
        }
      }

      return null;
    };
  }

  private validateRaceOrder(): ValidatorFn {
    return (control: AbstractControl): {[key: string]: any} => {
      if (this.isEmptyInputValue(control.value)) {
        return { tooSmall: true };
      }

      if (control.value <= 0) {
        return { tooSmall: true };
      }

      if (control.value > this.races.length + 1) {
        return { tooLarge: true };
      }

      for (let race of this.races) {
        if (race.raceOrder === control.value) {
          return { existingNumber: true };
        }
      }

      return null;
    };
  }
}
