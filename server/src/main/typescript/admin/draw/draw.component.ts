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
import { FileProcessingResult } from '../entries/FileProcessingResult.model';

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

  public newDraw: File = null;
  public uploadDrawError: String = null;

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
    event.preventDefault();
    event.stopPropagation();
    this.updateEntries(this.draggedEntry, droppedOnEntry);
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

  public onFileChange(event: any): void {
    console.log(event);
    console.log(event.srcElement.files);
    if (event && event.srcElement && event.srcElement.files && event.srcElement.files.length > 0) {
      this.newDraw = event.srcElement.files[0];
      console.log(this.newDraw);
    } else {
      this.newDraw = null;
    }
  }

  public uploadDraw(): void {
    console.log("will try upload of " + this.newDraw);
    this.entriesService.uploadDraw(this.newDraw).then((status: FileProcessingResult) => {
      if (status.code == 200) {
        this.newDraw = null;
        this.uploadDrawError = null;
        this.clubsService.refresh();
        this.entriesService.refresh();
      } else {
        this.uploadDrawError = status.data;
        console.log(status.data);
      }
    });
  }

  public getEventNameForEntry(entry: Entry): string {
      let eventName: string;
      const event: Event = this.getEventForId(entry.event);
      if (event) {
          eventName = event.name;
      }
      return eventName;
  }

  public getClubNamesForEntry(entry: Entry): string {
      let clubs: string;
      for (const clubId of entry.clubs) {
          const club: Club = this.getClubForId(clubId);
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

  private updateEntries(entryBeingDropped: Entry, droppedOnEntry: Entry): void {
    console.log('update');
    console.log(droppedOnEntry);
    console.log(entryBeingDropped);
    const indexOfDroppedOnEntry: number = this.entriesForRace.indexOf(droppedOnEntry);
    const indexOfEntryBeingDropped: number = this.entriesForRace.indexOf(entryBeingDropped);
    console.log(indexOfDroppedOnEntry);
    console.log(indexOfEntryBeingDropped);
    if (indexOfDroppedOnEntry === indexOfEntryBeingDropped) {
      console.log('dropped on self');
      return;
    }

    const entryRacePosition: number = 1;
    const entryNumber: number = 1;
    let updatedEntries: Entry[];
    if (droppedOnEntry === null) {
      updatedEntries = this.droppedOnHeader(entryBeingDropped);
    } else {
      updatedEntries = this.droppedOnEntry(entryBeingDropped, droppedOnEntry);
    }

    this.sortEntriesbyRacePosition(this.entriesForRace);
    console.log(updatedEntries);
    console.log(this.entriesForRace);
    if (updatedEntries.length > 0) {
      this.entriesService.updateEntries(updatedEntries);
    }
  }

  private droppedOnHeader(entryBeingDropped: Entry): Entry[] {
    console.log('dropped on table header');
    const updatedEntries: Entry[] = new Array<Entry>();
    const indexOfEntryBeingDropped: number = this.entriesForRace.indexOf(entryBeingDropped);
    if (!entryBeingDropped.fixedNumber) {
      entryBeingDropped.number = 1;
    }
    entryBeingDropped.raceOrder = 1;
    updatedEntries.push(entryBeingDropped);
    for (let i = 0; i < indexOfEntryBeingDropped; i++) {
      const updatedEntry: Entry = this.entriesForRace[i];
      if (!entryBeingDropped.fixedNumber) {
        this.incrementEntryNumber(updatedEntry);
      }
      this.incrementEntryRacePosition(updatedEntry);
      updatedEntries.push(updatedEntry);
    }

    return updatedEntries;
  }

  private droppedOnEntry(entryBeingDropped: Entry, droppedOnEntry: Entry): Entry[] {
    console.log('dropped on entry');
    const updatedEntries: Entry[] = new Array<Entry>();
    const indexOfDroppedOnEntry: number = this.entriesForRace.indexOf(droppedOnEntry);
    const indexOfEntryBeingDropped: number = this.entriesForRace.indexOf(entryBeingDropped);
    if (indexOfDroppedOnEntry < indexOfEntryBeingDropped) {
      this.updateDroppedEntryDetails(entryBeingDropped, indexOfDroppedOnEntry, true);
      updatedEntries.push(entryBeingDropped);
      for (let i = indexOfDroppedOnEntry + 1; i < indexOfEntryBeingDropped; i++) {
        const updatedEntry: Entry = this.entriesForRace[i];
        if (!entryBeingDropped.fixedNumber) {
          this.incrementEntryNumber(updatedEntry);
        }
        this.incrementEntryRacePosition(updatedEntry);
        updatedEntries.push(updatedEntry);
      }
    } else {
      this.updateDroppedEntryDetails(entryBeingDropped, indexOfDroppedOnEntry, false);
      updatedEntries.push(entryBeingDropped);
      for (let i = indexOfEntryBeingDropped + 1; i < indexOfDroppedOnEntry + 1; i++) {
        const updatedEntry: Entry = this.entriesForRace[i];
        if (!entryBeingDropped.fixedNumber) {
          this.decrementEntryNumber(updatedEntry);
        }
        this.decrementEntryRacePosition(updatedEntry);
        updatedEntries.push(updatedEntry);
      }
    }

    return updatedEntries;
  }

  private sortEntriesbyRacePosition(entries: Entry[]): Entry[] {
     return entries.sort((a: Entry, b: Entry) => {
          if (a.raceOrder < b.raceOrder) {
              return -1;
          } else if (a.raceOrder === b.raceOrder) {
              return 0;
          } else if (a.raceOrder > b.raceOrder) {
              return 1;
          }
      });
  }

  private updateDroppedEntryDetails(
    entry: Entry, indexOfDroppedOn: number, increment: boolean): void {
    const droppedOnRacePosition: number = 0;
    const droppedOnEntry: Entry = this.entriesForRace[indexOfDroppedOn];
    if (increment) {
      entry.raceOrder = droppedOnEntry.raceOrder + 1;
    } else {
      entry.raceOrder = droppedOnEntry.raceOrder;
    }
    if (entry.fixedNumber) {
      console.log('fixed number');
      return;
    }

    let entryNumber: number = 0;
    for (let i = indexOfDroppedOn; i >= 0; i--) {
      const prevEntry: Entry = this.entriesForRace[i];
      if (!prevEntry.fixedNumber) {
        if (increment) {
          entryNumber = prevEntry.number + 1;
        } else {
          entryNumber = prevEntry.number;
        }
        break;
      }
    }
    if (entryNumber !== 0) {
      console.log(entryNumber);
      entry.number = entryNumber;
    }
  }

  private incrementEntryRacePosition(entry: Entry): void {
    entry.raceOrder = entry.raceOrder + 1;
  }

  private decrementEntryRacePosition(entry: Entry): void {
    entry.raceOrder = entry.raceOrder - 1;
  }

  private incrementEntryNumber(entry: Entry): void {
    if (!entry.fixedNumber) {
      entry.number = entry.number + 1;
    }
  }

  private decrementEntryNumber(entry: Entry): void {
    if (!entry.fixedNumber) {
      entry.number = entry.number - 1;
    }
  }

  private getEventForId(eventId: number): Event {
      for (const event of this.events) {
          if (event.id === eventId) {
              return event;
          }
      }
      return null;
  }

  private getClubForId(clubId: number): Club {
      for (const club of this.clubs) {
          if (club.id === clubId) {
              return club;
          }
      }
      return null;
  }

  private getEntriesForRace(race: Race): Entry[] {
    const entries: Entry[] = this.entries.filter((entry: Entry) => entry.race === race.id);
    this.sortEntriesbyRacePosition(entries);
    return entries;
  }
}
