import { Component } from '@angular/core';

import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';

import { RacesService } from '../../services/races.service';
import { Race } from '../../models/race.model';
import { Observable } from 'rxjs/Observable';

@Component({
  selector: 'landing',
  styleUrls: [ './landing.component.css' ],
  templateUrl: './landing.component.html'
})
export class LandingComponent {
    date: Date = new Date();
    model: NgbDateStruct = {year: this.date.getFullYear(), month: this.date.getMonth() + 1, day: this.date.getDate()};
    races: Observable<Race[]>;

    constructor(private racesService: RacesService) {
        this.races = this.racesService.getRaces();
    }

  ngOnInit() {
    console.log('hello from landing');
  }

}
