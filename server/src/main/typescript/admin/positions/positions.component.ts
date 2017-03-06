import { Component, OnInit } from '@angular/core';

import { Position } from '../../positions/position.model';
import { AdminPositionsService } from './positions.service';

@Component({
  selector: 'positions',
  styleUrls: [ './positions.component.css' ],
  templateUrl: './positions.component.html'
})
export class PositionsComponent implements OnInit {

  public positions: Position[];

  constructor(private positionsService: AdminPositionsService) {}

  public ngOnInit() {
    console.log('hello from positions');
    this.positionsService.getPositions()
      .subscribe((positions: Position[]) => this.positions = positions);
  }

}
