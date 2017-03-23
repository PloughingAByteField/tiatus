import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';

import { Race } from '../../races/race.model';

import { AdminRacesService } from '../races/races.service';

@Component({
  selector: 'events',
  styleUrls: [ './events.component.css' ],
  templateUrl: './events.component.html'
})
export class EventsComponent implements OnInit, OnDestroy {

  private racesSubscription: any;

  constructor(
    private racesService: AdminRacesService,
    private router: Router
  ) {}

  public ngOnInit() {
    this.racesSubscription = this.racesService.getRaces()
      .subscribe((races: Race[]) => {
        if (races.length > 0) {
          this.changedRace(races[0]);
        }
    });
  }

  public ngOnDestroy() {
    this.racesSubscription.unsubscribe();
  }

  private changedRace(race: Race): void {
    this.router.navigate(['/events', race.id]);
  }
}
