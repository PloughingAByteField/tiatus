import { Component, OnInit } from '@angular/core';

import { Club } from '../../clubs/club.model';
import { AdminClubsService } from './clubs.service';

@Component({
  selector: 'clubs',
  styleUrls: [ './clubs.component.css' ],
  templateUrl: './clubs.component.html'
})
export class ClubsComponent implements OnInit {
  public clubs: Club[];

  constructor(private clubsService: AdminClubsService) {}
  public ngOnInit() {
    console.log('hello from clubs');
    this.clubsService.getClubs()
      .subscribe((clubs: Club[]) => this.clubs = clubs);
  }

}
