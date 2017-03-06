import { Injectable } from '@angular/core';

import { AdminUsersHttpService } from './users-http.service';

@Injectable()
export class AdminUsersService {

    constructor(private service: AdminUsersHttpService) {}

}
