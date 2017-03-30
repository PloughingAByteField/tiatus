import { Component, OnInit, OnDestroy } from '@angular/core';
import { Location } from '@angular/common';

import { Subscription } from 'rxjs/Subscription';

import { Race } from '../../../races/race.model';
import { Entry } from '../../../entries/entry.model';

import { SelectedRaceService } from '../../races/selected-race.service';
import { AdminRacesService } from '../../races/races.service';

@Component({
  selector: 'edit-entry',
  styleUrls: [ './edit-entry.component.css' ],
  templateUrl: './edit-entry.component.html'
})
export class EditEntryComponent implements OnInit, OnDestroy {

  public model: Entry = new Entry();
  public selectedRace: Race;

  private racesSubscription: Subscription;
  private races: Race[];

  constructor(
    private racesService: AdminRacesService,
    private selectedRaceService: SelectedRaceService,
    private location: Location
  ) {}

  public ngOnInit() {
    this.selectedRace = this.selectedRaceService.getSelectedRace.value;
    this.racesSubscription = this.racesService.getRaces()
      .subscribe((races: Race[]) => this.races = races);
  }

  public ngOnDestroy() {
    this.racesSubscription.unsubscribe();
  }

  public changeRace(race: Race): void {
    console.log(race);
  }

  public goBack(): void {
    this.location.back();
  }

}
