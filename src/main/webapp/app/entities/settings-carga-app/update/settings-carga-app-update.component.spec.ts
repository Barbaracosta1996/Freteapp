import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SettingsCargaAppFormService } from './settings-carga-app-form.service';
import { SettingsCargaAppService } from '../service/settings-carga-app.service';
import { ISettingsCargaApp } from '../settings-carga-app.model';

import { SettingsCargaAppUpdateComponent } from './settings-carga-app-update.component';

describe('SettingsCargaApp Management Update Component', () => {
  let comp: SettingsCargaAppUpdateComponent;
  let fixture: ComponentFixture<SettingsCargaAppUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let settingsCargaAppFormService: SettingsCargaAppFormService;
  let settingsCargaAppService: SettingsCargaAppService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SettingsCargaAppUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SettingsCargaAppUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SettingsCargaAppUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    settingsCargaAppFormService = TestBed.inject(SettingsCargaAppFormService);
    settingsCargaAppService = TestBed.inject(SettingsCargaAppService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const settingsCargaApp: ISettingsCargaApp = { id: 456 };

      activatedRoute.data = of({ settingsCargaApp });
      comp.ngOnInit();

      expect(comp.settingsCargaApp).toEqual(settingsCargaApp);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISettingsCargaApp>>();
      const settingsCargaApp = { id: 123 };
      jest.spyOn(settingsCargaAppFormService, 'getSettingsCargaApp').mockReturnValue(settingsCargaApp);
      jest.spyOn(settingsCargaAppService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ settingsCargaApp });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: settingsCargaApp }));
      saveSubject.complete();

      // THEN
      expect(settingsCargaAppFormService.getSettingsCargaApp).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(settingsCargaAppService.update).toHaveBeenCalledWith(expect.objectContaining(settingsCargaApp));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISettingsCargaApp>>();
      const settingsCargaApp = { id: 123 };
      jest.spyOn(settingsCargaAppFormService, 'getSettingsCargaApp').mockReturnValue({ id: null });
      jest.spyOn(settingsCargaAppService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ settingsCargaApp: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: settingsCargaApp }));
      saveSubject.complete();

      // THEN
      expect(settingsCargaAppFormService.getSettingsCargaApp).toHaveBeenCalled();
      expect(settingsCargaAppService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISettingsCargaApp>>();
      const settingsCargaApp = { id: 123 };
      jest.spyOn(settingsCargaAppService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ settingsCargaApp });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(settingsCargaAppService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
