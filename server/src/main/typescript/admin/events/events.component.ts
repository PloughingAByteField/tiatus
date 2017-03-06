import { Component, OnInit } from '@angular/core';

import { Event } from '../../events/event.model';
import { AdminEventsService } from './events.service';

@Component({
  selector: 'events',
  styleUrls: [ './events.component.css' ],
  templateUrl: './events.component.html'
})
export class EventsComponent implements OnInit {
  public events: Event[];

  constructor(private eventsService: AdminEventsService) {}

  public ngOnInit() {
    console.log('hello from events');
    this.eventsService.getEvents()
      .subscribe((events: Event[]) => this.events = events);
  }

}
