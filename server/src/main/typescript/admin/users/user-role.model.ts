import { Role, convertObjectToRole } from './role.model';

export class UserRole {
    public role: Role;
    public id: number;
}

export function convertObjectToUserRole(obj: UserRole): UserRole {
    if (obj) {
        let userRole: UserRole = new UserRole();
        userRole.id = obj.id;
        userRole.role = convertObjectToRole(obj.role);
        return userRole;
    }
    return null;
}
