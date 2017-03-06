import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'positions',
  styleUrls: [ './positions.component.css' ],
  templateUrl: './positions.component.html'
})
export class PositionsComponent implements OnInit {

  public ngOnInit() {
    console.log('hello from positions');
  }

}
