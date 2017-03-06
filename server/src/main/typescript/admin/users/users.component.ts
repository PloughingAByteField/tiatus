import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'users',
  styleUrls: [ './users.component.css' ],
  templateUrl: './users.component.html'
})
export class UsersComponent implements OnInit {

  public ngOnInit() {
    console.log('hello from users');
  }

}
