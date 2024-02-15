import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';

import { Subscription } from 'rxjs';

import { RaceEvent } from '../../race-events/race-event.model';
import { Event } from '../../events/event.model';
import { Race, convertObjectToRace } from '../../races/race.model';
import { AdminRacesService } from '../races/races.service';
import { AdminEventsService } from './events.service';
import { AdminUnassignedEventsService } from './unassigned-events.service';
import { AdminRaceEventsService } from './race-events.service';
import { SelectedRaceService } from '../races/selected-race.service';

@Component({
  selector: 'events',
  styleUrls: [ './events.component.css' ],
  templateUrl: './events.component.html'
})
export class EventsComponent implements OnInit, OnDestroy {

  public unassignedEvents: Event[] = new Array<Event>();

  public raceEvents: RaceEvent[] = new Array<RaceEvent>();
  public selectedRace: Race = null;
  public races: Race[] = new Array<Race>();

  private raceId: number;
  private eventsSubscription: Subscription;
  private raceEventsSubscription: Subscription;
  private racesSubscription: Subscription;
  private unassignedEventsSubscription: Subscription;
  private selectedRaceSubscription: Subscription;

  private events: Event[] = new Array<Event>();
  private raceEventsFromService: RaceEvent[] = new Array<RaceEvent>();

  constructor(
    private eventsService: AdminEventsService,
    private raceEventsService: AdminRaceEventsService,
    private unassignedEventsService: AdminUnassignedEventsService,
    private selectedRaceService: SelectedRaceService,
    private racesService: AdminRacesService,
    private router: Router
  ) {}

  public ngOnInit() {
    console.log("onInit");
    this.selectedRace = this.selectedRaceService.getSelectedRace.value;
    this.racesSubscription = this.racesService.getRaces()
      .subscribe((races: Race[]) => {
        if (races.length > 0) {
          for (let i = this.races.length; i > 0; i--) {
            this.races.pop();
          }
          races.map((race: Race) => {
            this.races.push(convertObjectToRace(race));
          });
          if (this.selectedRace === null) {
              this.setSelectedRace(this.races[0]);
          } else {
            this.selectedRace = this.getRaceFromRaces(this.selectedRace);
          }
          const unassigned: Race = new Race();
          unassigned.name = 'Unassigned';
          unassigned.id = 0;
          this.races.push(unassigned);
        }
        if (this.selectedRace) {
          this.getEventsForRace(this.selectedRace.id);
        }
    });

    this.eventsSubscription = this.eventsService.getEvents()
      .subscribe((events: Event[]) => {
        console.log("Got events ");
        console.log(events);
        this.events = events;
        if (this.selectedRace) {
          this.getEventsForRace(this.selectedRace.id);
        }
      });

    this.raceEventsSubscription = this.raceEventsService.getEvents()
      .subscribe((events: RaceEvent[]) => {
        console.log("race events " + events.length);
        if (events.length > 0) {
          this.raceEventsFromService = events;
          if (this.selectedRace) {
            console.log("have selected race " + this.selectedRace.name);
            this.getEventsForRace(this.selectedRace.id);
          }
        }
    });

    this.selectedRaceSubscription =
      this.selectedRaceService.getSelectedRace.subscribe((race: Race) => {
        if (race !== null) {
          console.log(race);
          this.changedRace(race);
        }
    });
  }

  public ngOnDestroy() {
    if (this.eventsSubscription) {
      this.eventsSubscription.unsubscribe();
    }
    if (this.raceEventsSubscription) {
      this.raceEventsSubscription.unsubscribe();
    }
    if (this.racesSubscription) {
      this.racesSubscription.unsubscribe();
    }
    if (this.unassignedEventsSubscription) {
      this.unassignedEventsSubscription.unsubscribe();
    }
    if (this.selectedRaceSubscription) {
        this.selectedRaceSubscription.unsubscribe();
    }
  }

  public getEventName(raceEvent: RaceEvent): string {
    const e = this.getEventForEventId(raceEvent.event);
    if (e !== null) {
      return e.name;
    }
    return null;
  }

  public editRaceEvent(raceEvent: RaceEvent): void {
    console.log(raceEvent);
    this.router.navigate(['/events/edit', raceEvent.event]);
  }

  public removeRaceEvent(raceEvent: RaceEvent): void {
    console.log(raceEvent);
    this.raceEventsService.removeRaceEvent(raceEvent);
  }

  public editUnassignedEvent(event: Event): void {
    console.log(event);
    this.router.navigate(['/events/edit', event.id]);
  }

  public removeUnassignedEvent(event: Event): void {
    console.log(event);
    this.unassignedEventsService.removeEvent(event);
  }

  public changeRace(race: Race): void {
    console.log('change race by ui');
    console.log(race);
    if (race.id !== 0) {
      this.setSelectedRace(race);
    } else {
      this.getEventsForRace(race.id);
    }
  }

  private changedRace(race: Race): void {
    console.log('changed race');
    console.log(race);
  }

  private setSelectedRace(race: Race): void {
    console.log(race);
    if (race === null) {
      return;
    }
    const localRace: Race = this.getRaceFromRaces(race);
    if (localRace !== undefined) {
      this.selectedRaceService.setSelectedRace = race;
      this.selectedRace = localRace;
    }
    this.getEventsForRace(this.selectedRace.id);
  }

  private getRaceFromRaces(race: Race): Race {
    return this.races.filter((r: Race) => r.id === race.id).shift();
  }

  private getEventsForRace(raceId: number): void {
    console.log("events for race " + raceId);
    if (raceId !== 0) {
      const race: Race = this.racesService.getRaceForId(raceId);
      console.log(raceId);
      console.log(race);
      this.raceEvents = this.raceEventsService.getEventsForRace(race);
      console.log(this.raceEvents);
    } else {
      if (!this.unassignedEventsSubscription) {
        this.unassignedEventsSubscription = this.unassignedEventsService.getEvents()
          .subscribe((unassigned: Event[]) => this.unassignedEvents = unassigned);
      }
    }
  }

  private getEventForEventId(id: number): Event {
    for (const event of this.events) {
      if (event.id === id) {
        return event;
      }
    }
    return null;
  }
}
