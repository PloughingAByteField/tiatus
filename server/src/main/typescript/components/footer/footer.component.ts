import { Component, OnInit } from '@angular/core';

import { ConfigService } from '../../config/config.service';

@Component({
  selector: 'footer',
  styleUrls: [ './footer.component.css' ],
  templateUrl: './footer.component.html'
})
export class FooterComponent implements OnInit {

  public footer: string;

  private tiatus: string = 'Tiatus';

  constructor(private configService: ConfigService) {
    this.footer = this.tiatus;
  }

  public ngOnInit() {
    this.configService.getFooter().subscribe((data: string) => {
      if (data) {
        this.footer = this.tiatus + ' / ' + data;
      }
    });
  }

  public getFooter(): string {
    return this.footer;
  }
}
