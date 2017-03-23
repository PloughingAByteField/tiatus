import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';

import { PositionsService } from '../../positions/positions.service';
import { RacesService } from '../../races/races.service';
import { Position } from '../../positions/position.model';

import { TimingPositionService } from '../times/timing-position.service';

@Component({
  selector: 'position',
  styleUrls: [ './position.component.css' ],
  templateUrl: './position.component.html'
})
export class PositionComponent implements OnInit {
    public date: Date = new Date();
    public model: NgbDateStruct = {
      year: this.date.getFullYear(),
      month: this.date.getMonth() + 1,
      day: this.date.getDate()
    };
    public positions: Observable<Position[]>;

    public selectedPosition: Position;

    constructor(
      private positionsService: PositionsService,
      private timingPositionService: TimingPositionService) {}

    public ngOnInit() {
      this.timingPositionService.getPosition.subscribe((position: Position) => {
        if (position) {
          this.selectedPosition = position;
        }
      });

      this.positions =  this.positionsService.getPositions();
    }

    public setSelectedPosition(position: Position): void {
      this.timingPositionService.setPosition = position;
    }

}
