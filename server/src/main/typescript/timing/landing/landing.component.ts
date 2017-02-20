import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';

import { PositionsService } from '../../services/positions.service';
import { RacesService } from '../../services/races.service';
import { Race } from '../../models/race.model';
import { Position } from '../../models/position.model';

import { TimingPositionService } from '../services/timing-position.service';

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
    public positions: Observable<Position[]>;

    private selectedPosition: Position;

    constructor(
      private racesService: RacesService,
      private positionsService: PositionsService,
      private timingPositionService: TimingPositionService) {}

    public ngOnInit() {
      console.log('hello from landing');

      this.timingPositionService.getPosition.subscribe((position: Position) => {
        if (position) {
          console.log('have stored position');
          console.log(position);
          this.selectedPosition = position;
        } else {
          console.log('do not have stored position');
        }
      });

      this.positions =  this.positionsService.getPositions();
    }

}
