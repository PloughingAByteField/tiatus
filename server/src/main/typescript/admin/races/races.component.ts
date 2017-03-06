import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'races',
  styleUrls: [ './races.component.css' ],
  templateUrl: './races.component.html'
})
export class RacesComponent implements OnInit {

  public ngOnInit() {
    console.log('hello from races');
  }

}
