import { Injectable } from '@angular/core';

import { AdminDrawHttpService } from './draw-http.service';

@Injectable()
export class AdminDrawService {

    constructor(private service: AdminDrawHttpService) {}

}
