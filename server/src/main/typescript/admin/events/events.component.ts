import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';

import { Event } from '../../events/event.model';
import { Race, convertJsonToRace } from '../../races/race.model';

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

  constructor(
    private racesService: AdminRacesService,
    private selectedRaceService: SelectedRaceService,
    private eventsService: AdminEventsService,
    private router: Router
  ) {}

  public ngOnInit() {
    console.log('init');
    this.eventsSubscription = this.eventsService.getEvents()
      .subscribe((events: Event[]) => this.events = events);

    this.selectedRaceSubscription =
      this.selectedRaceService.getSelectedRace.subscribe((race: Race) => {
        console.log(race);
        if (race !== null) {
          this.selectedRace = convertJsonToRace(race);
          this.changedRace(this.selectedRace);
        }
    });

    this.racesSubscription = this.racesService.getRaces()
      .subscribe((races: Race[]) => {
        console.log(races);
        if (races.length > 0) {
          for (let i = this.races.length; i > 0; i--) {
            this.races.pop();
          }
          races.map((race: Race) => {
            this.races.push(convertJsonToRace(race));
          });
          if (this.selectedRace === null) {
            this.selectedRaceService.setSelectedRace = races[0];
            this.selectedRace = this.races[0];
          } else {
            this.selectedRace = this.getRaceFromRaces(this.selectedRace);
          }
          let unassigned: Race = new Race();
          unassigned.name = 'Unassigned';
          unassigned.id = 0;
          this.races.push(unassigned);
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
    if (race.id !== 0) {
      this.selectedRaceService.setSelectedRace = this.racesService.getRaceForId(race.id);
      this.selectedRace = race;
    }
    this.router.navigate(['/events', race.id]);
  }

  private changedRace(race: Race): void {
    console.log('changed race');
    console.log(race);
    this.router.navigate(['/events', race.id]);

  }

  private getRaceFromRaces(race: Race): Race {
    return this.races.filter((r: Race) => r.id === race.id).shift();
  }
}
