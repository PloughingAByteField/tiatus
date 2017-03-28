import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';

import { Race } from '../../../races/race.model';

import { AdminRacesService } from '../../races/races.service';

@Component({
  selector: 'edit-entry',
  styleUrls: [ './edit-entry.component.css' ],
  templateUrl: './edit-entry.component.html'
})
export class EditEntryComponent implements OnInit, OnDestroy {

  private racesSubscription: any;
  private races: Race[];

  constructor(
    private racesService: AdminRacesService,
    private router: Router
  ) {}

  public ngOnInit() {
    this.racesSubscription = this.racesService.getRaces()
      .subscribe((races: Race[]) => this.races = races);
  }

  public ngOnDestroy() {
    this.racesSubscription.unsubscribe();
  }

}
