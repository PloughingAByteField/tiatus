import { Injectable } from '@angular/core';

import { PositionsService } from '../../positions/positions.service';
import { AdminPositionsHttpService } from './positions-http.service';

@Injectable()
export class AdminPositionsService extends PositionsService {

    constructor(protected service: AdminPositionsHttpService) {
        super(service);
    }
}
