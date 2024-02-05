import { Injectable } from '@angular/core';

import { BehaviorSubject } from 'rxjs';

import { Message } from '../../websocket/message.model';
import { MessageType } from '../../websocket/message-type.model';

import { User, convertObjectToUser } from './user.model';

import { AdminUsersHttpService } from './users-http.service';

@Injectable()
export class AdminUsersService {

    protected users: User[] = new Array<User>();
    protected subject: BehaviorSubject<User[]>
        = new BehaviorSubject<User[]>(this.users);

    constructor(private service: AdminUsersHttpService) {
        this.refresh();
    }

    public getRaceForId(id: number): User {
      return this.users.filter((user: User) => user.id === id).shift();
    }

    public getUsers(): BehaviorSubject<User[]> {
        console.log(this.users);
        return this.subject;
    }

    public createUser(user: User): Promise<User> {
        return new Promise((resolve) => this.service.createUser(user).then((c: User) => {
            this.users.push(c);
            this.subject.next(this.users);
            resolve(c);
        }));
    }

    public removeUser(user: User): void {
        this.service.removeUser(user).then((c: User) => {
            const index = this.users.indexOf(c);
            this.users.splice(index, 1);
            this.subject.next(this.users);
        });
    }

    public updateUser(user: User): void {
        this.service.updateUser(user).then((c: User) => {
            const updatedUser = this.getUserForId(c.id);
            if (updatedUser !== null) {
                updatedUser.userName = c.userName;
                updatedUser.roles = c.roles;
            }
            this.subject.next(this.users);
        });
    }

    public getUserForId(userId: number): User {
        for (const user of this.users) {
            if (user.id === userId) {
                return user;
            }
        }
        return null;
    }

    public refresh(): void {
         this.service.getUsers().subscribe((users: User[]) => {
            if (users != null) {
                this.users = users;
                this.subject.next(this.users);
            }
        });
    }

    public processUserMessage(message: Message): void {
        console.log('process message');
        const user: User = convertObjectToUser(message.data);
        console.log(user);
        if (message.type === MessageType.ADD) {
            this.users.push(user);
        } else if (message.type === MessageType.DELETE) {
            const deletedUser: User = this.getUserForId(user.id);
            if (deletedUser !== null) {
                const index = this.users.indexOf(deletedUser);
                const sliced = this.users.splice(index, 1);
            }
        } else if (message.type === MessageType.UPDATE) {
            const updatedUser: User = this.getUserForId(user.id);
            if (updatedUser !== null) {
                updatedUser.userName = user.userName;
                updatedUser.roles = user.roles;
            }
        }

        this.subject.next(this.users);
    }
}
