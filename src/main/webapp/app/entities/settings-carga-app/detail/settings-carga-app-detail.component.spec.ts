import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SettingsCargaAppDetailComponent } from './settings-carga-app-detail.component';

describe('SettingsCargaApp Management Detail Component', () => {
  let comp: SettingsCargaAppDetailComponent;
  let fixture: ComponentFixture<SettingsCargaAppDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SettingsCargaAppDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ settingsCargaApp: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SettingsCargaAppDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SettingsCargaAppDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load settingsCargaApp on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.settingsCargaApp).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
