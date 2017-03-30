import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';

import { Race } from '../../../races/race.model';
import { Entry } from '../../../entries/entry.model';

import { AdminRacesService } from '../../races/races.service';

@Component({
  selector: 'create-entry',
  styleUrls: [ './create-entry.component.css' ],
  templateUrl: './create-entry.component.html'
})
export class CreateEntryComponent implements OnInit, OnDestroy {

  public model: Entry = new Entry();

  private racesSubscription: any;
  private races: Race[];

  constructor(
    private racesService: AdminRacesService,
    private router: Router
  ) {}

  public ngOnInit() {
    console.log('hello for create');
    this.racesSubscription = this.racesService.getRaces()
      .subscribe((races: Race[]) => this.races = races);
  }

  public ngOnDestroy() {
    this.racesSubscription.unsubscribe();
  }

}
