import { Component, OnInit } from '@angular/core';

import { AdminConfigService } from './config.service';

@Component({
  selector: 'config',
  styleUrls: [ './config.component.css' ],
  templateUrl: './config.component.html'
})
export class ConfigComponent implements OnInit {

  public logo: string;
  public title: string;

  constructor(private configService: AdminConfigService) {}

  public getLogo(): string {
    return this.logo;
  }

  public getTitle(): string {
    return this.title;
  }

  public ngOnInit() {
    console.log('hello from config');
    if ( !this.logo) {
      this.configService.getLogo().subscribe((data: string) => {
        this.logo = data;
      });
    }
    if ( !this.title) {
      this.configService.getEventTitle().subscribe((data: string) => {
        this.title = data;
      });
    }
  }

}
