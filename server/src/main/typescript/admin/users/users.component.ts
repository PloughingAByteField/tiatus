import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormArray } from '@angular/forms';
import { Validators, AbstractControl, ValidatorFn } from '@angular/forms';

import { Subscription } from 'rxjs';

import { User } from './user.model';
import { UserRole } from './user-role.model';
import { Role } from './role.model';
import { AdminUsersService } from './users.service';
import { AdminRolesService } from './roles.service';

@Component({
  selector: 'users',
  styleUrls: [ './users.component.css' ],
  templateUrl: './users.component.html'
})
export class UsersComponent implements OnInit, OnDestroy {
  public users: User[] = new Array<User>();
  public roles: Role[] = new Array<Role>();
  public addUserForm: FormGroup;
  public usersForm: FormGroup;

  private usersSubscription: Subscription;
  private userRolesSubscription: Subscription;

  constructor(
    private usersService: AdminUsersService,
    private rolesService: AdminRolesService,
    private fb: FormBuilder
  ) {}

  public ngOnInit() {
    this.usersForm = this.fb.group({
      users: this.fb.array([])
    });

    this.addUserForm = this.fb.group({
      name: this.fb.control('', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(250),
        this.existingUserName()
      ]),
      password: this.fb.control('', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(250)
      ]),
      type: this.fb.control('')
    });

    this.usersSubscription = this.usersService.getUsers()
      .subscribe((users: User[]) => {
        if (users.length > 0) {
          this.users = users;
          console.log("populate users " + users.length);
          this.populateUsersForm(this.users);
        }
    });

    this.userRolesSubscription = this.rolesService.getRoles()
      .subscribe((roles: Role[]) => {
        this.roles = roles;
        if (this.roles.length > 0) {
          this.populateRolesDropDowns(this.roles);
        }
    });
  }

  public ngOnDestroy() {
    this.usersSubscription.unsubscribe();
    this.userRolesSubscription.unsubscribe();
  }

  public canUpdateUser(userControlGroup: FormGroup): boolean {
    const userToRemove: User = userControlGroup.get('user').value;
    console.log(userToRemove.userName);
    console.log(userControlGroup.valid);
    if (!userControlGroup.dirty) {
      return false;
    }

    if (userControlGroup.get('user') && userControlGroup.get('type')) {
      if (userControlGroup.get('type').dirty) {
        const userFromControl: User = userControlGroup.get('user').value;
        const roleIdFromControl: number = userControlGroup.get('type').value;
        if (userFromControl.getRole().id !== +roleIdFromControl) {
          console.log('role has changed');
          const adminRole: Role = this.getAdminRole();
          if (adminRole !== null && userFromControl.getRole().id === adminRole.id) {
            const adminUsers: User[] = this.users.filter((user: User) =>
                user.getRole().id === adminRole.id);
            if (adminUsers.length <= 1) {
                console.log('cannot change type of sole admin user');
                return false;
            }
          } else {
            console.log('user type has changed');
            return true;
          }
        }
      }
    }

    if (userControlGroup.get('password')) {
      if (userControlGroup.get('password').valid && userControlGroup.get('password').dirty) {
        console.log('password has changed');
        return true;
      }
    }

    if (userControlGroup.get('user') && userControlGroup.get('name')) {
      if (userControlGroup.get('name').dirty) {
        const userFromControl: User = userControlGroup.get('user').value;
        const nameFromControl: string = userControlGroup.get('name').value;
        if (nameFromControl !== userFromControl.userName) {
          console.log('user name has changed');
          return true;
        }
      }
    }

    return false;
  }

  public canRemoveUser(userControlGroup: FormGroup): boolean {
    const userToRemove: User = userControlGroup.get('user').value;
    const adminRole: Role = this.getAdminRole();
    if (adminRole !== null) {
      if (userToRemove.getRole().id === adminRole.id) {
        console.log(userToRemove.userName);
        const adminUsers: User[] = this.users.filter((user: User) =>
          user.getRole().id === adminRole.id);
        if (adminUsers.length <= 1) {
          console.log('cannot remove sole admin user');
          return false;
        }
      }
    }
    return true;
  }

  public removeUser(userControlGroup: FormGroup): void {
    const userToRemove: User = userControlGroup.get('user').value;
    this.usersService.removeUser(userToRemove);
  }

  public updateUser(userControlGroup: FormGroup): void {
    const updatedName: string = userControlGroup.get('name').value;
    const updatedPassword: string = userControlGroup.get('password').value;
    const userToUpdate: User = userControlGroup.get('user').value;
    const updatedRole: Role = this.rolesService.getRoleForId(+userControlGroup.get('type').value);
    const updatedUser: User = new User();
    updatedUser.userName = updatedName;
    updatedUser.password = updatedPassword;
    updatedUser.id = userToUpdate.id;
    updatedUser.roles = new Array<UserRole>();
    const userRole: UserRole = new UserRole();
    userRole.role = updatedRole;
    updatedUser.roles.push(userRole);
    this.usersService.updateUser(updatedUser);
  }

  public onSubmit({ value, valid }: { value: any, valid: boolean }) {
    console.log(value);
    const newUser: User = new User();
    newUser.userName = value.name;
    newUser.password = value.password;
    newUser.roles = new Array<UserRole>();
    const userRole: UserRole = new UserRole();
    userRole.role = value.type;
    newUser.roles.push(userRole);
    console.log(newUser);
    this.usersService.createUser(newUser).then((user: User) => {
      this.addUserForm.reset();
      this.addUserForm.get('type').setValue(this.roles[0]);
    });
  }

  private populateRolesDropDowns(roles: Role[]): void {
    console.log(roles);
    if (roles.length > 0) {
      this.addUserForm.get('type').setValue(roles[0]);
    }
  }

  private populateUsersForm(users: User[]): void {
    if (users.length > 0) {
      const newUsersArray: FormArray = this.fb.array([]);
      users.map((user: User) => {
        console.log("user is " + user.userName);
        const userControl: FormControl = this.fb.control(user);

        const nameControl: FormControl = this.fb.control(user.userName, [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(250)
        ]);

        const passwordControl: FormControl = this.fb.control('', [
          Validators.minLength(8),
          Validators.maxLength(250)
        ]);

        console.log("get role "+ user.getRole());
        const typeControl: FormControl = this.fb.control(user.getRole().id);

        const group: FormGroup = this.fb.group({
          name: nameControl,
          user: userControl,
          password: passwordControl,
          type: typeControl
        });
        group.setValidators(
          Validators.compose([
            this.existingUserName()
          ])
        );

        newUsersArray.push(group);
      });
      this.usersForm.setControl('users', newUsersArray);
    }
  }

  private getAdminRole(): Role {
    for (const role of this.roles) {
      if (role.roleName === 'ADMIN') {
        return role;
      }
    }
    return null;
  }

  private existingUserName(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (control.get('user') && control.get('name')) {
        const userFromControl: User = control.get('user').value;
        const nameFromControl: string = control.get('name').value;
        console.log(userFromControl);
        if (this.users) {
          for (const user of this.users) {
            if (userFromControl.userName !== user.userName && user.userName === nameFromControl) {
              console.log('match for ' + nameFromControl);
              return { existingUserName: true };
            }
          }
        }
      }

      return null;
    };
  }
}
