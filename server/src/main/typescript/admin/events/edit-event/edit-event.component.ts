import { Component, OnInit, OnDestroy } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute, Params } from '@angular/router';
import { FormBuilder, FormControl, FormGroup, FormArray } from '@angular/forms';
import { Validators, AbstractControl, ValidatorFn } from '@angular/forms';

import { Subscription } from 'rxjs/Subscription';

import { Race } from '../../../races/race.model';

import { Event } from '../../../events/event.model';
import { EventPosition } from '../../../events/event-positions.model';
import { AdminEventsService } from '../events.service';

import { Position } from '../../../positions/position.model';
import { AdminPositionsService } from '../../positions/positions.service';

import { RaceEvent } from '../../../race-events/race-event.model';
import { AdminUnassignedEventsService } from '../unassigned-events.service';

import { AdminRaceEventsService } from '../race-events.service';
import { AdminRacesService } from '../../races/races.service';

@Component({
  selector: 'edit-event',
  styleUrls: [ './edit-event.component.css' ],
  templateUrl: './edit-event.component.html'
})
export class EditEventComponent implements OnInit, OnDestroy {

  public editEventForm: FormGroup;

  public selectedEvent: Event;
  public positions: Position[] = new Array<Position>();
  public unassignedPositions: Position[] = new Array<Position>();

  private eventId: number;
  private eventInRace: Race;
  private events: Event[] = new Array<Event>();
  private eventsInRace: Event[] = new Array<Event>();
  private races: Race[] = new Array<Race>();
  private raceEvents: RaceEvent[] = new Array<RaceEvent>();
  private unassignedEvents: Event[] = new Array<Event>();

  private positionsSubscription: Subscription;
  private eventsSubscription: Subscription;
  private unassignedEventsSubscription: Subscription;
  private raceEventsSubscription: Subscription;
  private racesSubscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private positionsService: AdminPositionsService,
    private racesService: AdminRacesService,
    private eventsService: AdminEventsService,
    private unassignedEventsService: AdminUnassignedEventsService,
    private raceEventsService: AdminRaceEventsService,
    private fb: FormBuilder,
    private location: Location
  ) {}

  public ngOnInit() {
    this.editEventForm = this.fb.group({
      name: this.fb.control('', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(250),
        this.existingEventName()
      ]),
      isNonRaceAssigned: this.fb.control(false),
      newPosition: this.fb.control(''),
      positions: this.fb.array([])
    });

    this.route.params.subscribe((params: Params) => {
        this.eventId = +params['eventId'];
        console.log('this.eventId ' + this.eventId);
        this.setSelectedEvent(this.getEventForId(this.eventId));
    });

    this.positionsSubscription = this.positionsService.getPositions()
      .subscribe((positions: Position[]) => {
        this.positions = positions;
    });

    this.eventsSubscription = this.eventsService.getEvents()
      .subscribe((events: Event[]) => {
        this.events = events;
        if (events.length > 0 && this.eventId) {
          this.setSelectedEvent(this.getEventForId(this.eventId));
        }
    });

    this.racesSubscription = this.racesService.getRaces()
      .subscribe((races: Race[]) => {
        this.races = races;
        if (races.length > 0) {
          this.populateEventsForRace(this.selectedEvent);
        }
    });

    this.raceEventsSubscription = this.raceEventsService.getEvents()
      .subscribe((events: RaceEvent[]) => {
        this.raceEvents = events;
        if (events.length > 0) {
          this.populateEventsForRace(this.selectedEvent);
        }
    });

    this.unassignedEventsSubscription = this.unassignedEventsService.getEvents()
      .subscribe((events: Event[]) => this.unassignedEvents = events);
  }

  public ngOnDestroy() {
    this.positionsSubscription.unsubscribe();
    this.eventsSubscription.unsubscribe();
    this.unassignedEventsSubscription.unsubscribe();
    this.raceEventsSubscription.unsubscribe();
    this.racesSubscription.unsubscribe();
  }

  public onSubmitN(value: any): void {
    console.log(value);
  }

  public onSubmit({ value, valid }: { value: any, valid: boolean }) {
    console.log('on submit');
    // create event
    // let event: Event = new Event();
    // event.name = value.name;
    // event.positions = new Array<EventPosition>();
    // for (let position of value.positions) {
    //   let eventPosition: EventPosition = new EventPosition();
    //   eventPosition.position = position.position;
    //   eventPosition.positionOrder = position.positionOrder;
    //   event.positions.push(eventPosition);
    // }
    // if (value.isNonRaceAssigned) {
    //   this.eventsService.createEvent(event).then((newEvent: Event) => {
    //     console.log(newEvent);
    //     this.unassignedEventsService.addEvent(newEvent);
    //     this.goBack();
    //   });

    // } else {
//      let raceEvent: RaceEventPojo = new RaceEventPojo();
//      raceEvent.race = this.selectedRace.id;
//      raceEvent.event = event;
//      this.raceEventsService.createRaceEvent(raceEvent).then((newRaceEvent: RaceEvent) => {
//        this.goBack();
//      });
    // }
  }

  public removePosition(index: number): void {
    let array: FormArray = this.editEventForm.get('positions') as FormArray;
    for (let i = index + 1; i < array.controls.length; i++) {
      let group: FormGroup = array.controls[i] as FormGroup;
      let orderValue: number = group.get('positionOrder').value;
      let orderControl: AbstractControl = group.get('positionOrder');
      orderControl.setValue(orderValue - 1);
    }
    let group: FormGroup = array.controls[index] as FormGroup;
    array.removeAt(index);
    group.removeControl('position');
    group.removeControl('positionOrder');
    group.removeControl('eventPosition');
    this.fillUnassignedPositions();
  }

  public addPosition(): void {
    let newPosition: Position = this.editEventForm.get('newPosition').value;
    let array: FormArray = this.editEventForm.get('positions') as FormArray;
    let newEventPosition: EventPosition = new EventPosition();
    newEventPosition.event = this.selectedEvent.id;
    newEventPosition.position = newPosition.id;
    newEventPosition.positionOrder = 1;
    if (array.controls.length > 0) {
      newEventPosition.positionOrder =
        array.controls[array.controls.length - 1].get('positionOrder').value + 1;
    }
    let eventPositionControl: FormControl = this.fb.control(newEventPosition);
    let positionControl: FormControl = this.fb.control(newPosition.id);
    let positionOrderControl: FormControl =
      this.fb.control(newEventPosition.positionOrder);
    let group: FormGroup = this.fb.group({
      eventPosition: eventPositionControl,
      position: positionControl,
      positionOrder: positionOrderControl
    });
    array.push(group);

    // remove from unassigned
    this.unassignedPositions.splice(this.unassignedPositions.indexOf(newPosition), 1);
    this.fillUnassignedPositions();

    // have form revalidate
    group.markAsDirty();
  }

  public goBack(): void {
    this.location.back();
  }

  public getPositionNameForPositionId(positionId: number) {
    for (let position of this.positions) {
      if (position.id === positionId) {
        return position.name;
      }
    }
    return null;
  }

  public canEnableSubmitButton(): boolean {
    if (!this.editEventForm.dirty) {
      return false;
    }
    if (this.editEventForm.valid) {
      console.log(this.editEventForm.get('name').dirty);
      if (this.editEventForm.get('name').dirty) {
        if (this.editEventForm.get('name').value !== this.selectedEvent.name) {
          console.log('ss');
          return true;
        }
        console.log('aa');
      }

      console.log(this.editEventForm.get('positions').dirty);
      let array: FormArray = this.editEventForm.get('positions') as FormArray;
      if (this.editEventForm.get('positions').dirty) {
        if (array.controls.length !== this.selectedEvent.positions.length) {
          console.log('have position addition/removal');
          return true;
        }
        for (let group of array.controls) {
          if (group.dirty) {
            let eventPosition: EventPosition = group.get('eventPosition').value;
            console.log(eventPosition);
            let position: number = group.get('position').value;
            console.log(position);
            if (eventPosition.position !== +position) {
              console.log('changed position');
              return true;
            }
            let index: number = array.controls.indexOf(group);
            console.log('index is ' + index);
            if (this.selectedEvent.positions[index].position !== +position) {
              console.log('mismatched positions');
              return true;
            }
          }
        }
      } else {
        console.log('not touched');
      }

      if (array.controls.length < 2) {
        return false;
      }
    }

    return false;
  }

  private getRaceForId(raceId: number): Race {
    for (let race of this.races) {
      if (race.id === raceId) {
        return race;
      }
    }
    return null;
  }

  private getRaceContainingEvent(event: Event): Race {
    for (let raceEvent of this.raceEvents) {
      if (raceEvent.event === event.id) {
        return this.getRaceForId(raceEvent.race);
      }
    }
    return null;
  }

  private populateEventsForRace(event: Event): void {
    if (event !== null) {
      this.eventInRace = this.getRaceContainingEvent(event);
      if (this.eventInRace !== null) {
        let raceEvents: RaceEvent[] = this.raceEvents
          .filter((raceEvent: RaceEvent) => raceEvent.race === this.eventInRace.id);
        this.eventsInRace = new Array<Event>();
        raceEvents.map((raceEvent: RaceEvent) =>
          this.eventsInRace.push(this.getEventForId(raceEvent.event)));
      }
    }
  }

  private setSelectedEvent(event: Event): void {
    this.selectedEvent = event;
    if (this.selectedEvent !== null) {
      this.populateEventsForRace(this.selectedEvent);
      this.editEventForm.get('name').setValue(event.name);
      let positionsFormArray: FormArray = this.fb.array([]);
      for (let eventPosition of this.selectedEvent.positions) {
        let eventPositionControl: FormControl = this.fb.control(eventPosition);
        let positionControl: FormControl = this.fb.control(eventPosition.position);
        let positionOrderControl: FormControl = this.fb.control(eventPosition.positionOrder);
        let group: FormGroup = this.fb.group({
          eventPosition: eventPositionControl,
          position: positionControl,
          positionOrder: positionOrderControl
        });
        positionsFormArray.push(group);
      }
      this.editEventForm.setControl('positions', positionsFormArray);
      this.fillUnassignedPositions();
    }
  }

  private getEventForId(eventId: number): Event {
    if (this.events) {
      for (let event of this.events) {
        if (event.id === eventId) {
          return event;
        }
      }
    }
    return null;
  }

  private fillUnassignedPositions(): void {
    this.unassignedPositions = this.positions.filter((position: Position) =>
          !this.positionInForm(position));
    if (this.unassignedPositions.length > 0) {
      this.editEventForm.setControl('newPosition', this.fb.control(this.unassignedPositions[0]));
    }
  }

  private positionInForm(position: Position): boolean {
    let array: FormArray = this.editEventForm.get('positions') as FormArray;
    for (let control of array.controls) {
      let p: EventPosition = control.get('eventPosition').value;
      if (position.id === p.position) {
        return true;
      }
    }
    return false;
  }

  private checkExistingPosition(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      // let position: number;
      // if (control.get('position')) {
      //   position = +control.get('position').value as number;
      // }
      // let positionOrder: number;
      // if (control.get('positionOrder')) {
      //   positionOrder = +control.get('positionOrder').value as number;
      // }
      // let entry: RacePositionTemplateEntry;
      // if (control.get('entry')) {
      //   entry = control.get('entry').value;
      // }
      // if (entry) {
      //   if (+entry.position === +position && +entry.positionOrder !== +positionOrder) {
      //     return { samePosition: true };
      //   }
      // }
      // let array: FormArray =  this.editEventForm.get('positions') as FormArray;
      // for (let group of array.controls) {
      //   if (control !== group) {
      //     let groupPositon: number = +group.get('position').value as number;
      //     if (position === groupPositon) {
      //       return { existingPosition: true };
      //     }
      //   }
      // }
      // // now check to see if matching against the backing
      // // templates when 2 positions have been swapped
      // if (this.selectedTemplate && entry) {
      //   for (let template of this.selectedTemplate.templates) {
      //     if (entry.position !== template.position) {
      //       if (+template.position === +position) {
      //         return { existingPosition: true };
      //       }
      //     }
      //   }
      // }
      return null;
    };
  }

  private existingEventName(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.selectedEvent) {
        for (let event of this.eventsInRace) {
          if (event.name === control.value && event.name !== this.selectedEvent.name) {
            console.log('match for ' + event.name);
            return { existingName: true };
          }
        }
      }
      // check to see if check box is ticked if so check unassigned else check assigned for race
      // if (this.editEventForm && this.editEventForm.get('isNonRaceAssigned')) {
      //   let isNonRaceAssigned: boolean = this.editEventForm.get('isNonRaceAssigned').value;
      //   if (isNonRaceAssigned) {
      //     for (let unassigned of this.unassignedEvents) {
      //       if (unassigned.name === control.value) {
      //         return { existingName: true };
      //       }
      //     }
      //   } else {
      //     for (let event of this.eventsForRace) {
      //       if (event.name === control.value) {
      //         return { existingName: true };
      //       }
      //     }
      //   }
      // }
      return null;
    };
  }
}
