import { Injectable, OnDestroy } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subscription } from 'rxjs/Subscription';

import { Event, convertObjectToEvent } from './event.model';
import { Race } from '../races/race.model';

import { EventsHttpService } from './events-http.service';

import { Message } from '../websocket/message.model';
import { MessageType } from '../websocket/message-type.model';

@Injectable()
export class EventsService implements OnDestroy {
    protected events: Event[] = new Array<Event>();
    protected subject: BehaviorSubject<Event[]>
        = new BehaviorSubject<Event[]>(this.events);
    protected requested: boolean = false;

    private subscription: Subscription;

    constructor(protected service: EventsHttpService) {}

    public ngOnDestroy() {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
    }

    public getEvents(): BehaviorSubject<Event[]> {
        if (!this.requested) {
            this.requested = true;
            this.refresh();
        }
        return this.subject;
    }

    public refresh(): void {
        this.subscription = this.service.getEvents().subscribe((events: Event[]) => {
            this.events = events;
            this.subject.next(this.events);
        });
    }

    public processMessage(message: Message): void {
        console.log('process message');
        console.log(message);
        const event: Event = convertObjectToEvent(message.data);
        console.log(event);
        if (message.type === MessageType.ADD) {
            this.events.push(event);

        } else if (message.type === MessageType.DELETE) {
            const deletedEvent: Event = this.getEventForId(event.id);
            if (deletedEvent !== null) {
                const index = this.events.indexOf(deletedEvent);
                const sliced = this.events.splice(index, 1);
            }

        } else if (message.type === MessageType.UPDATE) {
            const updatedEvent: Event = this.getEventForId(event.id);
            if (updatedEvent !== null) {
                updatedEvent.name = event.name;
                updatedEvent.positions = event.positions;
                updatedEvent.weighted = event.weighted;
            }
        }

        this.subject.next(this.events);
    }

    public getEventForId(eventId: number): Event {
        for (const event of this.events) {
            if (event.id === eventId) {
                return event;
            }
        }
        return null;
    }
}
