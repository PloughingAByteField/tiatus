import { Component, OnInit, OnDestroy } from '@angular/core';
import { Location } from '@angular/common';
import { FormBuilder, FormControl, FormGroup, FormArray } from '@angular/forms';
import { Validators, AbstractControl, ValidatorFn } from '@angular/forms';

import { Subscription } from 'rxjs';

import { Race } from '../../../races/race.model';
import { Entry } from '../../../entries/entry.model';
import { Event } from '../../../events/event.model';
import { Club } from '../../../clubs/club.model';
import { RaceEvent } from '../../../race-events/race-event.model';

import { SelectedRaceService } from '../../races/selected-race.service';
import { AdminRacesService } from '../../races/races.service';
import { AdminEventsService } from '../../events/events.service';
import { AdminClubsService } from '../../clubs/clubs.service';
import { AdminEntriesService } from '../entries.service';
import { AdminUnassignedEventsService } from '../../events/unassigned-events.service';
import { AdminRaceEventsService } from '../../events/race-events.service';

@Component({
  selector: 'create-entry',
  styleUrls: [ './create-entry.component.css' ],
  templateUrl: './create-entry.component.html'
})
export class CreateEntryComponent implements OnInit, OnDestroy {

  public addEntryForm: FormGroup;
  public selectedRace: Race;
  public eventsForRace: Event[] = new Array<Event>();
  public unassignedEvents: Event[] = new Array<Event>();
  public raceEvents: RaceEvent[] = new Array<RaceEvent>();
  public clubs: Club[] = new Array<Club>();

  public entriesForRace: Entry[] = new Array<Entry>();

  private events: Event[] = new Array<Event>();
  private entries: Entry[] = new Array<Entry>();
  private racesSubscription: Subscription;
  private eventsSubscription: Subscription;
  private clubsSubscription: Subscription;
  private entriesSubscription: Subscription;
  private raceEventsSubscription: Subscription;
  private unassignedEventsSubscription: Subscription;
  private races: Race[];

  constructor(
    private racesService: AdminRacesService,
    private selectedRaceService: SelectedRaceService,
    private eventsService: AdminEventsService,
    private clubsService: AdminClubsService,
    private entriesService: AdminEntriesService,
    private raceEventsService: AdminRaceEventsService,
    private unassignedEventsService: AdminUnassignedEventsService,
    private fb: FormBuilder,
    private location: Location
  ) {}

  public ngOnInit() {
    this.addEntryForm = this.fb.group({
      crew: this.fb.control('', [
        Validators.minLength(1),
        Validators.maxLength(250),
      ]),
      isFixedNumber: this.fb.control(false),
      eventsType: this.fb.control('current'),
      clubs: this.fb.control('', Validators.required),
      event: this.fb.control(''),
      race: this.fb.control(''),
      fixedNumber: this.fb.control('', this.validateEntryNumber()),
      weighting: this.fb.control(''),
    });

    this.selectedRace = this.selectedRaceService.getSelectedRace.value;
    this.racesSubscription = this.racesService.getRaces()
      .subscribe((races: Race[]) => {
        this.races = races;
        if (races.length > 0) {
          if (!this.selectedRace) {
            this.selectedRace = races[0];
          }
          this.addEntryForm.get('race').setValue(this.selectedRace);
          this.entriesForRace = this.getEntriesForRace(this.selectedRace);
          this.eventsForRaceChange(this.selectedRace);
        }
    });

    this.eventsSubscription = this.eventsService.getEvents().subscribe((events: Event[]) => {
        this.events = events;
    });

    this.clubsSubscription = this.clubsService.getClubs()
      .subscribe((clubs: Club[]) => {
        this.clubs = clubs;
    });

    this.entriesSubscription = this.entriesService.getEntries()
      .subscribe((entries: Entry[]) => {
        this.entries = entries;
        if (this.selectedRace) {
          this.entriesForRace = this.getEntriesForRace(this.selectedRace);
        }
    });

    this.raceEventsSubscription = this.raceEventsService.getEvents()
      .subscribe((events: RaceEvent[]) => {
        if (events.length > 0) {
          this.raceEvents = events;
          if (this.selectedRace) {
            this.eventsForRaceChange(this.selectedRace);
          }
        }
    });

    this.unassignedEventsSubscription = this.unassignedEventsService.getEvents()
          .subscribe((unassigned: Event[]) => this.unassignedEvents = unassigned);
  }

  public ngOnDestroy() {
    this.racesSubscription.unsubscribe();
    this.eventsSubscription.unsubscribe();
    this.clubsSubscription.unsubscribe();
    this.entriesSubscription.unsubscribe();
    this.raceEventsSubscription.unsubscribe();
    this.unassignedEventsSubscription.unsubscribe();
  }

  public onSubmit({ value, valid }: { value: any, valid: boolean }) {
    console.log('on submit');
    console.log(value);
    const entry: Entry = new Entry();
    entry.event = value.event.id;
    entry.race = value.race.id;
    entry.clubs = new Array<number>();
    for (const club of value.clubs) {
      entry.clubs.push(club.id);
    }
    if (value.crew.length > 0) {
      entry.crew = value.crew;
    }
    entry.fixedNumber = value.fixedNumber;
    if (value.isFixedNumber === true) {
      entry.number = value.fixedNumber;
    } else {
      if (this.entriesForRace.length > 0) {
        const lastEntry: Entry = this.entriesForRace[this.entriesForRace.length - 1];
        entry.number = lastEntry.number + 1;
        entry.raceOrder = lastEntry.raceOrder + 1;
      } else {
        entry.number = 1;
        entry.raceOrder = 1;
      }
    }
    console.log(entry);
    this.entriesService.createEntry(entry).then((newEntry: Entry) => {
      this.goBack();
    });
  }

  public changeRace(race: Race): void {
    console.log(race);
    this.selectedRace = race;
    this.entriesForRace = this.getEntriesForRace(this.selectedRace);
    this.eventsForRaceChange(this.selectedRace);
    if (this.addEntryForm.get('isFixedNumber')) {
      this.addEntryForm.get('isFixedNumber').setValue(false);
    }
    if (this.addEntryForm.get('fixedNumber')) {
      this.addEntryForm.get('fixedNumber').setValue('');
    }
  }

  public goBack(): void {
    this.location.back();
  }

  public onEventTypeChange(type: string): void {
    console.log('change');
    console.log(type);
    switch (type) {
      case 'current':
        console.log('got current');
        this.setEventsToCurrentRace();
        break;
      case 'unassigned':
        console.log('got unassigned');
        this.setEventsToUnassigned();
        break;
      case 'other':
        console.log('got other');
        this.setEventsToOtherRaces();
        break;
      default:
        console.log('got unknown');
        break;
    }
  }

  public getRaceNameForEvent(event: Event): string {
    if (!this.addEntryForm.get('eventsType')) {
      return null;
    }
    if (this.addEntryForm.get('eventsType').value === 'unassigned') {
      return 'unassigned';
    }
    for (const raceEvent of this.raceEvents) {
      if (raceEvent.event === event.id) {
        return this.racesService.getRaceForId(raceEvent.race).name;
      }
    }
    return null;
  }

  private setEventsToCurrentRace(): void {
    this.eventsForRace = this.getEventsForRace(this.selectedRace);
    if (this.eventsForRace.length > 0) {
      this.addEntryForm.get('event').setValue(this.eventsForRace[0]);
    }
  }

  private setEventsToOtherRaces(): void {
    this.eventsForRace = this.getEventsNotInRace(this.selectedRace);
    if (this.eventsForRace.length > 0) {
      this.addEntryForm.get('event').setValue(this.eventsForRace[0]);
    }
  }

  private setEventsToUnassigned(): void {
    this.eventsForRace = this.unassignedEvents;
    if (this.eventsForRace.length > 0) {
      this.addEntryForm.get('event').setValue(this.eventsForRace[0]);
    }
  }

  private eventsForRaceChange(race: Race): void {
    this.onEventTypeChange(this.addEntryForm.get('eventsType').value);
  }

  private getEntriesForRace(race: Race): Entry[] {
    return this.entries
      .filter((entry: Entry) => entry.race === race.id);
  }

  private getEventsForRace(race: Race): Event[] {
    const raceEventsForRace: RaceEvent[] = this.raceEvents
      .filter((raceEvent: RaceEvent) => raceEvent.race === race.id);
    const eventsForRace: Event[] = new Array<Event>();
    raceEventsForRace.map((raceEvent: RaceEvent) => {
      for (const event of this.events) {
        if (event.id === raceEvent.event) {
          eventsForRace.push(event);
          break;
        }
      }
    });
    return eventsForRace;
  }

  private getEventsNotInRace(race: Race): Event[] {
    const raceEventsForRace: RaceEvent[] = this.raceEvents
      .filter((raceEvent: RaceEvent) => raceEvent.race !== race.id);
    const eventsForRace: Event[] = new Array<Event>();
    raceEventsForRace.map((raceEvent: RaceEvent) => {
      for (const event of this.events) {
        if (event.id === raceEvent.event) {
          eventsForRace.push(event);
          break;
        }
      }
    });
    return eventsForRace;
  }

  private validateEntryNumber(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.addEntryForm && this.addEntryForm.get('isFixedNumber')
        && this.addEntryForm.get('isFixedNumber').value === true) {
        console.log(control);
        if (this.isEmptyInputValue(control.value)) {
          return { tooSmall: true };
        }

        if (control.value <= 0) {
          return { tooSmall: true };
        }

        if (control.value >= 100000) {
          return { tooLarge: true };
        }

        for (const entry of this.entriesForRace) {
          if (entry.number === control.value) {
            console.log(entry);
            return { existingNumber: true };
          }
        }
      }

      return null;
    };
  }

  private isEmptyInputValue(value: any): boolean {
    return value == null || value.length === 0;
  }
}
