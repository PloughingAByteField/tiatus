import { EventEmitter, Injectable } from '@angular/core';

import { Position } from '../models/position.model';

@Injectable()
export class TimingPositionService {
    public position: Position;
}
