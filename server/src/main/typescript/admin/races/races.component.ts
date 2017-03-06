import { Component, OnInit } from '@angular/core';

import { Race } from '../../races/race.model';
import { AdminRacesService } from './races.service';

@Component({
  selector: 'races',
  styleUrls: [ './races.component.css' ],
  templateUrl: './races.component.html'
})
export class RacesComponent implements OnInit {
  public model: Race = new Race();
  public existingName: boolean = false;
  public existingNumber: boolean = false;
  public invalidNumber: boolean = false;

  public races: Race[];

  constructor(private racesService: AdminRacesService) {}
  public ngOnInit() {
    console.log('hello from races');
    this.racesService.getRaces()
      .subscribe((races: Race[]) => {
        this.races = races;
        this.model.raceOrder = this.races.length + 1;
      });
  }

  public addRace(model: Race): void {
    console.log(model);
  }

  public checkName(model: Race): void {
    this.existingName = false;
    let existingName: Race = this.races.filter((race: Race) => race.name === model.name).shift();
    if (existingName) {
      this.existingName = true;
    }
  }

  public checkNumber(model: Race): void {
    this.invalidNumber = false;
    if (model.raceOrder < 0 || model.raceOrder > this.races.length + 1) {
      console.log('invalid number');
      this.invalidNumber = true;
      return;
    }

    this.existingNumber = false;
    let existingOrder: Race = this.races
      .filter((race: Race) => race.raceOrder === model.raceOrder).shift();
    if (existingOrder) {
      this.existingNumber = true;
    }

  }
}
