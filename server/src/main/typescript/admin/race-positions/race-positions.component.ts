import { Component, OnInit } from '@angular/core';

import { Club } from '../../clubs/club.model';
import { RacePositionsService } from './race-positions.service';

@Component({
  selector: 'race-positions',
  styleUrls: [ './race-positions.component.css' ],
  templateUrl: './race-positions.component.html'
})
export class RacePositionsComponent implements OnInit {
  public clubs: Club[];

  constructor(private clubsService: RacePositionsService) {}

  public ngOnInit() {
    console.log('hello from race positions');
    this.clubsService.getClubs()
      .subscribe((clubs: Club[]) => this.clubs = clubs);
  }

}
