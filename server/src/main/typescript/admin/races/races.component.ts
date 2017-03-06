import { Component, OnInit } from '@angular/core';

import { Race } from '../../races/race.model';
import { AdminRacesService } from './races.service';

@Component({
  selector: 'races',
  styleUrls: [ './races.component.css' ],
  templateUrl: './races.component.html'
})
export class RacesComponent implements OnInit {

  public races: Race[];

  constructor(private racesService: AdminRacesService) {}
  public ngOnInit() {
    console.log('hello from races');
    this.racesService.getRaces()
      .subscribe((races: Race[]) => this.races = races);
  }

}
