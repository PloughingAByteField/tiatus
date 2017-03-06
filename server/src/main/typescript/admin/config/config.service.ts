import { Injectable } from '@angular/core';

import { ConfigService } from '../../config/config.service';
import { AdminConfigHttpService } from './config-http.service';

@Injectable()
export class AdminConfigService extends ConfigService {

    constructor(protected service: AdminConfigHttpService) {
        super(service);
    }
}
