import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SettingsContractsFormService } from './settings-contracts-form.service';
import { SettingsContractsService } from '../service/settings-contracts.service';
import { ISettingsContracts } from '../settings-contracts.model';

import { SettingsContractsUpdateComponent } from './settings-contracts-update.component';

describe('SettingsContracts Management Update Component', () => {
  let comp: SettingsContractsUpdateComponent;
  let fixture: ComponentFixture<SettingsContractsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let settingsContractsFormService: SettingsContractsFormService;
  let settingsContractsService: SettingsContractsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SettingsContractsUpdateComponent],
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
      .overrideTemplate(SettingsContractsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SettingsContractsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    settingsContractsFormService = TestBed.inject(SettingsContractsFormService);
    settingsContractsService = TestBed.inject(SettingsContractsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const settingsContracts: ISettingsContracts = { id: 456 };

      activatedRoute.data = of({ settingsContracts });
      comp.ngOnInit();

      expect(comp.settingsContracts).toEqual(settingsContracts);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISettingsContracts>>();
      const settingsContracts = { id: 123 };
      jest.spyOn(settingsContractsFormService, 'getSettingsContracts').mockReturnValue(settingsContracts);
      jest.spyOn(settingsContractsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ settingsContracts });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: settingsContracts }));
      saveSubject.complete();

      // THEN
      expect(settingsContractsFormService.getSettingsContracts).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(settingsContractsService.update).toHaveBeenCalledWith(expect.objectContaining(settingsContracts));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISettingsContracts>>();
      const settingsContracts = { id: 123 };
      jest.spyOn(settingsContractsFormService, 'getSettingsContracts').mockReturnValue({ id: null });
      jest.spyOn(settingsContractsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ settingsContracts: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: settingsContracts }));
      saveSubject.complete();

      // THEN
      expect(settingsContractsFormService.getSettingsContracts).toHaveBeenCalled();
      expect(settingsContractsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISettingsContracts>>();
      const settingsContracts = { id: 123 };
      jest.spyOn(settingsContractsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ settingsContracts });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(settingsContractsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
