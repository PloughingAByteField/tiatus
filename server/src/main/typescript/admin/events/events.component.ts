import { Component, OnInit, OnDestroy } from '@angular/core';

import { Event } from '../../events/event.model';
import { Race } from '../../races/race.model';

import { AdminEventsService } from './events.service';
import { AdminRacesService } from '../races/races.service';
import { SelectedRaceService } from '../races/selected-race.service';

@Component({
  selector: 'events',
  styleUrls: [ './events.component.css' ],
  templateUrl: './events.component.html'
})
export class EventsComponent implements OnInit, OnDestroy {
  public events: Event[];
  public races: Race[] = new Array<Race>();
  public selectedRace: Race = null;

  private eventsSubscription: any;
  private racesSubscription: any;
  private selectedRaceSubscription: any;
  private raceId: number = null;

  constructor(
    private racesService: AdminRacesService,
    private selectedRaceService: SelectedRaceService,
    private eventsService: AdminEventsService
  ) {}

  public ngOnInit() {
    this.eventsSubscription = this.eventsService.getEvents()
      .subscribe((events: Event[]) => this.events = events);

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
        if (this.races.length > 0) {
          if (this.selectedRace === null && this.raceId === null) {
            this.selectedRaceService.setSelectedRace = this.races[0];
          }
          if (this.raceId) {
            this.selectedRaceService.setSelectedRace = this.racesService.getRaceForId(this.raceId);
          }
        }
    });
  }

  public ngOnDestroy() {
    this.eventsSubscription.unsubscribe();
    this.racesSubscription.unsubscribe();
    this.selectedRaceSubscription.unsubscribe();
  }

  public changeRace(race: Race): void {
    console.log('change race by ui');
    console.log(race);
    this.selectedRaceService.setSelectedRace = race;
  }

  private changedRace(race: Race): void {
    console.log('changed race');
    console.log(race);
  }
}
