import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'events',
  styleUrls: [ './events.component.css' ],
  templateUrl: './events.component.html'
})
export class EventsComponent implements OnInit {

  public ngOnInit() {
    console.log('hello from events');
  }

}
