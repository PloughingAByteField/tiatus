import { Component, OnInit } from '@angular/core';

import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';

import { RacesService } from '../../services/races.service';
import { Race } from '../../models/race.model';
import { Observable } from 'rxjs/Observable';

@Component({
  selector: 'landing',
  styleUrls: [ './landing.component.css' ],
  templateUrl: './landing.component.html'
})
export class LandingComponent implements OnInit {
    public date: Date = new Date();
    public model: NgbDateStruct = {
      year: this.date.getFullYear(),
      month: this.date.getMonth() + 1,
      day: this.date.getDate()
    };
    public races: Observable<Race[]>;

    constructor(private racesService: RacesService) {}

    public ngOnInit() {
      console.log('hello from landing');
      this.races = this.racesService.getRaces();
    }

}
