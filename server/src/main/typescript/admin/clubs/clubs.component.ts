import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'clubs',
  styleUrls: [ './clubs.component.css' ],
  templateUrl: './clubs.component.html'
})
export class ClubsComponent implements OnInit {

  public ngOnInit() {
    console.log('hello from clubs');
  }

}
