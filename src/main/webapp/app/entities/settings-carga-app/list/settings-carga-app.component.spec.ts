import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { SettingsCargaAppService } from '../service/settings-carga-app.service';

import { SettingsCargaAppComponent } from './settings-carga-app.component';

describe('SettingsCargaApp Management Component', () => {
  let comp: SettingsCargaAppComponent;
  let fixture: ComponentFixture<SettingsCargaAppComponent>;
  let service: SettingsCargaAppService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'settings-carga-app', component: SettingsCargaAppComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [SettingsCargaAppComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(SettingsCargaAppComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SettingsCargaAppComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SettingsCargaAppService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.settingsCargaApps?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to settingsCargaAppService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getSettingsCargaAppIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getSettingsCargaAppIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
