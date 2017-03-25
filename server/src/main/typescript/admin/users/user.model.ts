import { UserRole, convertObjectToUserRole } from './user-role.model';
import { Role } from './role.model';

export class User {
    public userName: string;
    public password: string;
    public roles: UserRole[] = new Array<UserRole>();
    public id: number;

    public getRole(): Role {
        if (this.roles.length > 0) {
            return this.roles[0].role;
        }
        return null;
    }
}

export function convertObjectToUser(obj: User): User {
    if (obj) {
        let user: User = new User();
        user.userName = obj.userName;
        user.roles = new Array<UserRole>();
        obj.roles.map((userRole: UserRole) =>
            user.roles.push(convertObjectToUserRole(userRole)));
        user.id = obj.id;
        return user;
    }
    return null;
}
