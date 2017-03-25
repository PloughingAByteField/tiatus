export class Role {
    public roleName: string;
    public id: number;
}

export function convertObjectToRole(obj: Role): Role {
    if (obj) {
        let role: Role = new Role();
        role.roleName = obj.roleName;
        role.id = obj.id;
        return role;
    }
    return null;
}
