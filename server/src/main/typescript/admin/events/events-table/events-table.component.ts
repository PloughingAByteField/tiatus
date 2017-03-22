import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { Subscription } from 'rxjs/Subscription';

import { RaceEvent } from '../../../race-events/race-event.model';
import { Event } from '../../../events/event.model';
import { Race } from '../../../races/race.model';
import { AdminRacesService } from '../../races/races.service';
import { AdminEventsService } from '../events.service';
import { AdminUnassignedEventsService } from '../unassigned-events.service';
import { AdminRaceEventsService } from '../race-events.service';

@Component({
  selector: 'events-table',
  styleUrls: [ './events-table.component.css' ],
  templateUrl: './events-table.component.html'
})
export class EventsTableComponent implements OnInit, OnDestroy {

  public unassignedEvents: Event[] = new Array<Event>();

  public raceEvents: RaceEvent[] = new Array<RaceEvent>();
  public race: Race = null;

  private raceId: number;
  private eventsSubscription: Subscription;
  private raceEventsSubscription: Subscription;
  private racesSubscription: Subscription;
  private unassignedEventsSubscription: Subscription;
  private events: Event[] = new Array<Event>();
  private raceEventsFromService: RaceEvent[] = new Array<RaceEvent>();

  constructor(
    private route: ActivatedRoute,
    private eventsService: AdminEventsService,
    private raceEventsService: AdminRaceEventsService,
    private unassignedEventsService: AdminUnassignedEventsService,
    private racesService: AdminRacesService,
  ) {}

  public ngOnInit() {
    this.route.params.subscribe((params: Params) => {
        this.raceId = +params['raceId'];
        this.race = this.racesService.getRaceForId(this.raceId);
        if (this.race === null && this.racesService.getRaces().value.length === 0) {
          this.racesSubscription = this.racesService.getRaces()
            .subscribe((races: Race[]) => {
              this.race = this.racesService.getRaceForId(this.raceId);
              this.getEventsForRace(this.raceId);
            });
        }
    });

    this.eventsSubscription = this.eventsService.getEvents()
      .subscribe((events: Event[]) => this.events = events);

    this.raceEventsSubscription = this.raceEventsService.getEvents()
      .subscribe((events: RaceEvent[]) => {
        if (events.length > 0) {
          this.raceEventsFromService = events;
          this.getEventsForRace(this.raceId);
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
  }

  public getEventName(raceEvent: RaceEvent): string {
    let e = this.getEventForEventId(raceEvent.event);
    if (e !== null) {
      return e.name;
    }
    return null;
  }

  public editRaceEvent(raceEvent: RaceEvent): void {
    console.log(raceEvent);
  }

  public removeRaceEvent(raceEvent: RaceEvent): void {
    console.log(raceEvent);
  }

  public editUnassignedEvent(event: Event): void {
    console.log(event);
  }

  public removeUnassignedEvent(event: Event): void {
    console.log(event);
  }

  private getEventsForRace(raceId: number): void {
    if (raceId !== 0) {
      let race: Race = this.racesService.getRaceForId(raceId);
      this.raceEvents = this.raceEventsService.getEventsForRace(race);
    } else {
      if (!this.unassignedEventsSubscription) {
        this.unassignedEventsSubscription = this.unassignedEventsService.getEvents()
          .subscribe((unassigned: Event[]) => this.unassignedEvents = unassigned);
      }
    }
  }

  private getEventForEventId(id: number): Event {
    for (let event of this.events) {
      if (event.id === id) {
        return event;
      }
    }
    return null;
  }
}
