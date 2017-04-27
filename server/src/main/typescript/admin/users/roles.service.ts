import { Injectable } from '@angular/core';

import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { Role } from './role.model';

import { AdminRolesHttpService } from './roles-http.service';

@Injectable()
export class AdminRolesService {

    protected roles: Role[] = new Array<Role>();
    protected subject: BehaviorSubject<Role[]>
        = new BehaviorSubject<Role[]>(this.roles);

    constructor(private service: AdminRolesHttpService) {
        this.refresh();
    }

    public getRoles(): BehaviorSubject<Role[]> {
        return this.subject;
    }

    public getRoleForId(roleId): Role {
        for (const role of this.roles) {
            if (role.id === roleId) {
                return role;
            }
        }
        return null;
    }

    public refresh(): void {
         this.service.getRoles().subscribe((roles: Role[]) => {
            this.roles = roles;
            this.subject.next(this.roles);
        });
    }

}
