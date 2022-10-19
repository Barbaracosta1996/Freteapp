import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MenuItem} from "primeng/api";
import {Router} from "@angular/router";

@Component({
  selector: 'jhi-panel-menu',
  templateUrl: './panel-menu.component.html',
  styleUrls: ['./panel-menu.component.scss']
})
export class PanelMenuComponent implements OnInit {

  visible: boolean = false;

  items: MenuItem[] = [];

  constructor(private router: Router) { }

  ngOnInit(): void {
    this.items = [
      {
        label: 'Ofertas',
        items: [{
          label: 'Update',
          icon: 'pi pi-car',
          command: () => {

          }
        }]
      },
      {
        label: 'Viagens',
        items: [{
          label: 'Pesquisar Viagens',
          icon: 'pi pi-directions',
          command: () => {

          }
        }]
      }
    ];
  }

  showMenu() {
    this.visible = !this.visible;
  }

  open(page: string): void{
    this.visible = false;
    if (page !== '') {
      this.router.navigate(['/portal', page]).then();
    } else {
      this.router.navigate(['/portal']).then();
    }
  }

}
