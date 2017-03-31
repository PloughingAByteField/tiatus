import { Component, OnInit, OnDestroy, ElementRef } from '@angular/core';

import { Subscription } from 'rxjs/Subscription';

import { Race } from '../../races/race.model';
import { Entry } from '../../entries/entry.model';
import { Event } from '../../events/event.model';
import { Club } from '../../clubs/club.model';
import { RaceEvent } from '../../race-events/race-event.model';
import { AdminRacesService } from '../races/races.service';
import { AdminEntriesService } from '../entries/entries.service';
import { AdminEventsService } from '../events/events.service';
import { AdminClubsService } from '../clubs/clubs.service';

import { AdminDrawService } from './draw.service';

@Component({
  selector: 'draw',
  styleUrls: [ './draw.component.css' ],
  templateUrl: './draw.component.html'
})
export class DrawComponent implements OnInit, OnDestroy {

  public selectedRace: Race;
  public races: Race[] = new Array<Race>();
  public entriesForRace: Entry[] = new Array<Entry>();

  private racesSubscription: Subscription;
  private entriesSubscription: Subscription;
  private eventsSubscription: Subscription;
  private clubsSubscription: Subscription;
  private entries: Entry[] = new Array<Entry>();
  private clubs: Club[] = new Array<Club>();
  private events: Event[] = new Array<Event>();
  private draggedEntry: Entry;

  constructor(
    private racesService: AdminRacesService,
    private entriesService: AdminEntriesService,
    private clubsService: AdminClubsService,
    private eventsService: AdminEventsService,
    private drawService: AdminDrawService
  ) {}

  public ngOnInit() {
    console.log('hello from draw');
    this.racesSubscription = this.racesService.getRaces()
      .subscribe((races: Race[]) => {
        this.races = races;
        if (races.length > 0) {
          this.changeRace(this.races[0]);
        }
    });

    this.clubsSubscription = this.clubsService.getClubs().subscribe((clubs: Club[]) => {
        this.clubs = clubs;
        if (this.selectedRace) {
          this.entriesForRace = this.getEntriesForRace(this.selectedRace);
        }
    });

    this.eventsSubscription = this.eventsService.getEvents().subscribe((events: Event[]) => {
        this.events = events;
        if (this.selectedRace) {
          this.entriesForRace = this.getEntriesForRace(this.selectedRace);
        }
    });

    this.entriesSubscription = this.entriesService.getEntries()
      .subscribe((entries: Entry[]) => {
        this.entries = entries;
        if (this.selectedRace) {
          this.entriesForRace = this.getEntriesForRace(this.selectedRace);
        }
    });
  }

  public ngOnDestroy() {
    this.clubsSubscription.unsubscribe();
    this.eventsSubscription.unsubscribe();
    this.entriesSubscription.unsubscribe();
    this.racesSubscription.unsubscribe();
  }

  public changeRace(race: Race): void {
    this.selectedRace = race;
    console.log(this.selectedRace);
    this.entriesForRace = this.getEntriesForRace(this.selectedRace);
  }

  public onDrop(event: DragEvent, droppedOnEntry: Entry): void {
    console.log('dropped');
    console.log(droppedOnEntry);
    console.log(this.draggedEntry);
    if (droppedOnEntry === null) {
      console.log('dropped on table header');
    } else {
      console.log('dropped on entry');
    }
    event.preventDefault();
    event.stopPropagation();
    this.draggedEntry = null;
  }

  public onDragOver(event: DragEvent): void {
    event.preventDefault();
  }

  public onDragStart(event: DragEvent, entry: Entry): void {
    console.log('dragstart');
    console.log(entry);
    this.draggedEntry = entry;
  }

  public getEventNameForEntry(entry: Entry): string {
      let eventName: string;
      let event: Event = this.getEventForId(entry.event);
      if (event) {
          eventName = event.name;
      }
      return eventName;
  }

  public getClubNamesForEntry(entry: Entry): string {
      let clubs: string;
      for (let clubId of entry.clubs) {
          let club: Club = this.getClubForId(clubId);
          if (club) {
              if (!clubs) {
                  clubs = club.clubName;
              } else {
                  clubs = clubs.concat(' / ');
                  clubs = clubs.concat(club.clubName);
              }
          }
      }
      return clubs;
  }

  private getEventForId(eventId: number): Event {
      for (let event of this.events) {
          if (event.id === eventId) {
              return event;
          }
      }
      return null;
  }

  private getClubForId(clubId: number): Club {
      for (let club of this.clubs) {
          if (club.id === clubId) {
              return club;
          }
      }
      return null;
  }

  private getEntriesForRace(race: Race): Entry[] {
    return this.entries.filter((entry: Entry) => entry.race === race.id);
  }
}
