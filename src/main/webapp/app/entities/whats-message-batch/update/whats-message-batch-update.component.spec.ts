import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WhatsMessageBatchFormService } from './whats-message-batch-form.service';
import { WhatsMessageBatchService } from '../service/whats-message-batch.service';
import { IWhatsMessageBatch } from '../whats-message-batch.model';

import { WhatsMessageBatchUpdateComponent } from './whats-message-batch-update.component';

describe('WhatsMessageBatch Management Update Component', () => {
  let comp: WhatsMessageBatchUpdateComponent;
  let fixture: ComponentFixture<WhatsMessageBatchUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let whatsMessageBatchFormService: WhatsMessageBatchFormService;
  let whatsMessageBatchService: WhatsMessageBatchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [WhatsMessageBatchUpdateComponent],
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
      .overrideTemplate(WhatsMessageBatchUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WhatsMessageBatchUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    whatsMessageBatchFormService = TestBed.inject(WhatsMessageBatchFormService);
    whatsMessageBatchService = TestBed.inject(WhatsMessageBatchService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const whatsMessageBatch: IWhatsMessageBatch = { id: 456 };

      activatedRoute.data = of({ whatsMessageBatch });
      comp.ngOnInit();

      expect(comp.whatsMessageBatch).toEqual(whatsMessageBatch);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWhatsMessageBatch>>();
      const whatsMessageBatch = { id: 123 };
      jest.spyOn(whatsMessageBatchFormService, 'getWhatsMessageBatch').mockReturnValue(whatsMessageBatch);
      jest.spyOn(whatsMessageBatchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ whatsMessageBatch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: whatsMessageBatch }));
      saveSubject.complete();

      // THEN
      expect(whatsMessageBatchFormService.getWhatsMessageBatch).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(whatsMessageBatchService.update).toHaveBeenCalledWith(expect.objectContaining(whatsMessageBatch));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWhatsMessageBatch>>();
      const whatsMessageBatch = { id: 123 };
      jest.spyOn(whatsMessageBatchFormService, 'getWhatsMessageBatch').mockReturnValue({ id: null });
      jest.spyOn(whatsMessageBatchService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ whatsMessageBatch: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: whatsMessageBatch }));
      saveSubject.complete();

      // THEN
      expect(whatsMessageBatchFormService.getWhatsMessageBatch).toHaveBeenCalled();
      expect(whatsMessageBatchService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWhatsMessageBatch>>();
      const whatsMessageBatch = { id: 123 };
      jest.spyOn(whatsMessageBatchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ whatsMessageBatch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(whatsMessageBatchService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
