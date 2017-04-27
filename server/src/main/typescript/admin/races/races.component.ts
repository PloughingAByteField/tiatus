import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormArray } from '@angular/forms';
import { Validators, AbstractControl, ValidatorFn } from '@angular/forms';

import { Race } from '../../races/race.model';
import { AdminRacesService } from './races.service';
import { ExistingRaceNameValidator } from './ExistingRaceNameValidator';

@Component({
  selector: 'races',
  styleUrls: ['./races.component.css'],
  templateUrl: './races.component.html'
})
export class RacesComponent implements OnInit {
  public addRaceForm: FormGroup;
  public racesForm: FormGroup;

  public races: Race[] = new Array<Race>();

  private alteredRaceName: string;

  constructor(private racesService: AdminRacesService) { }

  public ngOnInit() {
    this.addRaceForm = new FormGroup({
      name: new FormControl('', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(10),
        this.validateRaceName()
      ]),
      raceOrder: new FormControl(
          this.races.length > 0 ? this.races[this.races.length - 1].raceOrder + 1 : 1, [
        Validators.required, this.validateRaceOrder()
      ])
    });

    this.racesService.getRaces()
      .subscribe((races: Race[]) => {
        this.races = races;
        this.addRaceForm.patchValue({
          raceOrder: this.races.length > 0 ? this.races[this.races.length - 1].raceOrder + 1 : 1
        });
        this.racesForm = new FormGroup({
          races: new FormArray([])
        });
        this.races.map((race: Race) => this.addRaceToArray(race));
      });
  }

  public addRaceToArray(race: Race) {
    const orderControl: FormControl = new FormControl(race.raceOrder);
    const nameControl: FormControl = new FormControl(race.name, [
              Validators.required,
              Validators.minLength(3),
              Validators.maxLength(10),
              this.validateRaceName()]);
    // do not need to check the changes as validateRaceName wil kick in
    // nameControl.valueChanges
    //       .subscribe((data) => {
    //         this.onValueChanged(race, data);
    //       });
    const raceGroup = new FormGroup({
      name: nameControl,
      order: orderControl
    });

    const array: FormArray = this.racesForm.get('races') as FormArray;
    array.push(raceGroup);
  }

  get getRacesArray(): FormArray { return this.racesForm.get('races') as FormArray; }

  public removeRace(data: any): void {
    const race: Race = this.getRaceForOrder(data.value.order);
    if (race) {
      this.racesService.removeRace(race);
    }
  }

  public updateRace(data: any): void {
    const race: Race = this.getRaceForOrder(data.value.order);
    if (race) {
      race.name = data.value.name;
      this.racesService.updateRace(race);
    }
  }

  public onValueChanged(race: Race, data?: any) {
    console.log('data');
    console.log(data);
    console.log(race);
  }

  public onSubmit({ value, valid }: { value: Race, valid: boolean }) {
    const race: Race = new Race();
    race.name = value.name;
    race.raceOrder = value.raceOrder;
    this.racesService.addRace(race);
    this.addRaceForm.reset({
      name: '',
      raceOrder: race.raceOrder + 1
    });
  }

  private getRaceForOrder(order: number): Race {
    for (const race of this.races) {
        if (race.raceOrder === order) {
          return race;
        }
    }
    return null;
  }
  private isEmptyInputValue(value: any): boolean {
    return value == null || value.length === 0;
  }

  private validateRaceName(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      for (const race of this.races) {
        if (race.name === control.value) {
          return { existingName: true };
        }
      }

      return null;
    };
  }

  private validateRaceOrder(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isEmptyInputValue(control.value)) {
        return { tooSmall: true };
      }

      if (control.value <= 0) {
        return { tooSmall: true };
      }

      if (control.value > this.races.length + 1) {
        return { tooLarge: true };
      }

      for (const race of this.races) {
        if (race.raceOrder === control.value) {
          return { existingNumber: true };
        }
      }

      return null;
    };
  }
}
