import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Params, Router   } from '@angular/router';

import { Subscription } from 'rxjs/Subscription';

import { RaceEvent } from '../../../race-events/race-event.model';
import { Event } from '../../../events/event.model';
import { Race, convertJsonToRace } from '../../../races/race.model';
import { AdminRacesService } from '../../races/races.service';
import { AdminEventsService } from '../events.service';
import { AdminUnassignedEventsService } from '../unassigned-events.service';
import { AdminRaceEventsService } from '../race-events.service';
import { SelectedRaceService } from '../../races/selected-race.service';

@Component({
  selector: 'events-table',
  styleUrls: [ './events-table.component.css' ],
  templateUrl: './events-table.component.html'
})
export class EventsTableComponent implements OnInit, OnDestroy {

  public unassignedEvents: Event[] = new Array<Event>();

  public raceEvents: RaceEvent[] = new Array<RaceEvent>();
  public selectedRace: Race = null;
  public races: Race[] = new Array<Race>();

  private raceId: number;
  private eventsSubscription: Subscription;
  private raceEventsSubscription: Subscription;
  private racesSubscription: Subscription;
  private unassignedEventsSubscription: Subscription;
  private selectedRaceSubscription: any;

  private events: Event[] = new Array<Event>();
  private raceEventsFromService: RaceEvent[] = new Array<RaceEvent>();

  constructor(
    private route: ActivatedRoute,
    private eventsService: AdminEventsService,
    private raceEventsService: AdminRaceEventsService,
    private unassignedEventsService: AdminUnassignedEventsService,
    private selectedRaceService: SelectedRaceService,
    private racesService: AdminRacesService,
    private router: Router
  ) {}

  public ngOnInit() {
    this.route.params.subscribe((params: Params) => {
        this.raceId = +params['raceId'];
        console.log('this.raceId ' + this.raceId );
        this.setSelectedRace(this.racesService.getRaceForId(this.raceId));
        this.getEventsForRace(this.raceId);
    });

    this.racesSubscription = this.racesService.getRaces()
      .subscribe((races: Race[]) => {
        if (races.length > 0) {
          for (let i = this.races.length; i > 0; i--) {
            this.races.pop();
          }
          races.map((race: Race) => {
            this.races.push(convertJsonToRace(race));
          });
          if (this.selectedRace === null) {
            if (this.raceId) {
              this.setSelectedRace(this.racesService.getRaceForId(this.raceId));
            } else {
              this.setSelectedRace(this.races[0]);
            }
          } else {
            this.selectedRace = this.getRaceFromRaces(this.selectedRace);
          }
          let unassigned: Race = new Race();
          unassigned.name = 'Unassigned';
          unassigned.id = 0;
          this.races.push(unassigned);
        }
        if (this.raceId) {
          this.getEventsForRace(this.raceId);
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

    this.selectedRaceSubscription =
      this.selectedRaceService.getSelectedRace.subscribe((race: Race) => {
        if (race !== null) {
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
    let e = this.getEventForEventId(raceEvent.event);
    if (e !== null) {
      return e.name;
    }
    return null;
  }

  public editRaceEvent(raceEvent: RaceEvent): void {
    console.log(raceEvent);
    this.router.navigate(['/events/edit', raceEvent.id]);
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
      this.router.navigate(['/events', race.id]);
    }
  }

  private changedRace(race: Race): void {
    console.log('changed race');
    console.log(race);
    this.router.navigate(['/events', race.id]);
  }

  private setSelectedRace(race: Race): void {
    console.log(race);
    if (race === null) {
      return;
    }
    let localRace: Race = this.getRaceFromRaces(race);
    if (localRace !== undefined) {
      this.selectedRaceService.setSelectedRace = race;
      this.selectedRace = localRace;
    }
  }

  private getRaceFromRaces(race: Race): Race {
    return this.races.filter((r: Race) => r.id === race.id).shift();
  }

  private getEventsForRace(raceId: number): void {
    if (raceId !== 0) {
      let race: Race = this.racesService.getRaceForId(raceId);
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
    for (let event of this.events) {
      if (event.id === id) {
        return event;
      }
    }
    return null;
  }
}
