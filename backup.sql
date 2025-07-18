--
-- PostgreSQL database dump
--

-- Dumped from database version 17.0
-- Dumped by pg_dump version 17.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: admin_profiles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.admin_profiles (
    user_id uuid NOT NULL,
    first_name character varying(100) NOT NULL,
    last_name character varying(100) NOT NULL,
    society_id uuid,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.admin_profiles OWNER TO postgres;

--
-- Name: platform_admin_profiles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.platform_admin_profiles (
    user_id uuid NOT NULL,
    first_name character varying(100) NOT NULL,
    last_name character varying(100) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.platform_admin_profiles OWNER TO postgres;

--
-- Name: provider_responses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.provider_responses (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    request_id uuid NOT NULL,
    response character varying(255) NOT NULL,
    provider_id uuid NOT NULL,
    total_cost text,
    notes text,
    responded_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT provider_responses_response_check CHECK (((response)::text = ANY (ARRAY[('ACCEPTED'::character varying)::text, ('REJECTED'::character varying)::text, ('MODIFIED'::character varying)::text, ('QUOTED'::character varying)::text, ('OUT_FOR_SERVICE'::character varying)::text, ('COMPLETED'::character varying)::text])))
);


ALTER TABLE public.provider_responses OWNER TO postgres;

--
-- Name: request_media; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.request_media (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    request_id uuid NOT NULL,
    url character varying(500) NOT NULL,
    filename character varying(255),
    file_size integer,
    media_type character varying(255) NOT NULL,
    mime_type character varying(100),
    is_before_service boolean DEFAULT true,
    uploaded_by uuid,
    uploaded_by_type character varying(255),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT request_media_media_type_check CHECK (((media_type)::text = ANY (ARRAY[('IMAGE'::character varying)::text, ('VIDEO'::character varying)::text, ('DOCUMENT'::character varying)::text, ('AUDIO'::character varying)::text]))),
    CONSTRAINT request_media_uploaded_by_type_check CHECK (((uploaded_by_type)::text = ANY (ARRAY[('RESIDENT'::character varying)::text, ('PROVIDER'::character varying)::text])))
);


ALTER TABLE public.request_media OWNER TO postgres;

--
-- Name: request_status_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.request_status_history (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    request_id uuid NOT NULL,
    from_status character varying(255),
    to_status character varying(255) NOT NULL,
    changed_by uuid,
    changed_by_type character varying(255),
    reason character varying(100),
    notes text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT request_status_history_changed_by_type_check CHECK (((changed_by_type)::text = ANY (ARRAY[('RESIDENT'::character varying)::text, ('PROVIDER'::character varying)::text, ('ADMIN'::character varying)::text, ('SYSTEM'::character varying)::text])))
);


ALTER TABLE public.request_status_history OWNER TO postgres;

--
-- Name: resident_profiles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.resident_profiles (
    user_id uuid,
    first_name character varying(100) NOT NULL,
    last_name character varying(100) NOT NULL,
    apartment_number character varying(50),
    society_id uuid,
    emergency_contact character varying(15),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.resident_profiles OWNER TO postgres;

--
-- Name: service_categories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.service_categories (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(100) NOT NULL,
    description text,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.service_categories OWNER TO postgres;

--
-- Name: service_provider_categories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.service_provider_categories (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    service_provider_id uuid NOT NULL,
    category_id uuid NOT NULL,
    hourly_rate integer,
    min_charge integer,
    is_primary boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.service_provider_categories OWNER TO postgres;

--
-- Name: service_provider_profiles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.service_provider_profiles (
    user_id uuid NOT NULL,
    first_name character varying(100) NOT NULL,
    last_name character varying(100) NOT NULL,
    business_name character varying(200),
    description text,
    experience_years integer DEFAULT 0,
    is_verified boolean DEFAULT false,
    verification_date timestamp without time zone,
    rating numeric(3,2) DEFAULT 0.0,
    total_jobs_completed integer DEFAULT 0,
    base_service_charge integer,
    phone_secondary character varying(10),
    address text,
    city character varying(100),
    state character varying(100),
    pincode character varying(10),
    available_hours_start time without time zone,
    available_hours_end time without time zone,
    is_available boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT service_provider_profiles_rating_check CHECK (((rating >= (0)::numeric) AND (rating <= (5)::numeric)))
);


ALTER TABLE public.service_provider_profiles OWNER TO postgres;

--
-- Name: service_provider_societies; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.service_provider_societies (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    service_provider_id uuid NOT NULL,
    society_id uuid NOT NULL,
    is_preferred boolean DEFAULT false,
    approval_status character varying(20) DEFAULT 'PENDING'::character varying,
    approved_by uuid,
    approved_at timestamp without time zone,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT service_provider_societies_approval_status_check CHECK (((approval_status)::text = ANY ((ARRAY['PENDING'::character varying, 'APPROVED'::character varying, 'REJECTED'::character varying])::text[])))
);


ALTER TABLE public.service_provider_societies OWNER TO postgres;

--
-- Name: service_ratings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.service_ratings (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    request_id uuid NOT NULL,
    resident_id uuid NOT NULL,
    provider_id uuid NOT NULL,
    overall_rating integer NOT NULL,
    quality_rating integer,
    timeliness_rating integer,
    professionalism_rating integer,
    value_rating integer,
    feedback text,
    would_recommend boolean,
    service_completed_on_time boolean,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT service_ratings_overall_rating_check CHECK (((overall_rating >= 1) AND (overall_rating <= 5))),
    CONSTRAINT service_ratings_professionalism_rating_check CHECK (((professionalism_rating >= 1) AND (professionalism_rating <= 5))),
    CONSTRAINT service_ratings_quality_rating_check CHECK (((quality_rating >= 1) AND (quality_rating <= 5))),
    CONSTRAINT service_ratings_timeliness_rating_check CHECK (((timeliness_rating >= 1) AND (timeliness_rating <= 5))),
    CONSTRAINT service_ratings_value_rating_check CHECK (((value_rating >= 1) AND (value_rating <= 5)))
);


ALTER TABLE public.service_ratings OWNER TO postgres;

--
-- Name: service_requests; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.service_requests (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    resident_id uuid NOT NULL,
    provider_id uuid,
    society_id uuid NOT NULL,
    category_id uuid NOT NULL,
    description text NOT NULL,
    urgency character varying(255) DEFAULT 'MEDIUM'::character varying NOT NULL,
    preferred_date date,
    preferred_time_slot character varying(50),
    location_details text,
    contact_phone character varying(15),
    status character varying(255) DEFAULT 'DRAFT'::character varying NOT NULL,
    final_cost numeric(10,2),
    payment_method character varying(20),
    payment_status character varying(255) DEFAULT 'PENDING'::character varying,
    expires_at timestamp without time zone,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT service_requests_payment_method_check CHECK (((payment_method)::text = ANY ((ARRAY['CASH'::character varying, 'CARD'::character varying, 'UPI'::character varying, 'WALLET'::character varying])::text[]))),
    CONSTRAINT service_requests_payment_status_check CHECK (((payment_status)::text = ANY (ARRAY[('PENDING'::character varying)::text, ('PARTIAL'::character varying)::text, ('PAID'::character varying)::text, ('REFUNDED'::character varying)::text, ('FAILED'::character varying)::text]))),
    CONSTRAINT service_requests_status_check CHECK (((status)::text = ANY (ARRAY[('DRAFT'::character varying)::text, ('SUBMITTED'::character varying)::text, ('PROVIDER_REVIEW'::character varying)::text, ('QUOTED'::character varying)::text, ('SCHEDULED'::character varying)::text, ('IN_PROGRESS'::character varying)::text, ('COMPLETED'::character varying)::text, ('CANCELLED'::character varying)::text, ('REJECTED'::character varying)::text, ('EXPIRED'::character varying)::text]))),
    CONSTRAINT service_requests_urgency_check CHECK (((urgency)::text = ANY (ARRAY[('LOW'::character varying)::text, ('MEDIUM'::character varying)::text, ('HIGH'::character varying)::text, ('EMERGENCY'::character varying)::text])))
);


ALTER TABLE public.service_requests OWNER TO postgres;

--
-- Name: service_schedules; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.service_schedules (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    request_id uuid NOT NULL,
    provider_id uuid NOT NULL,
    scheduled_date timestamp without time zone NOT NULL,
    estimated_start_time timestamp without time zone NOT NULL,
    estimated_end_time timestamp without time zone NOT NULL,
    actual_start_time timestamp without time zone,
    actual_end_time timestamp without time zone,
    status character varying(20) DEFAULT 'SCHEDULED'::character varying,
    reschedule_reason text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT service_schedules_status_check CHECK (((status)::text = ANY ((ARRAY['SCHEDULED'::character varying, 'CONFIRMED'::character varying, 'IN_PROGRESS'::character varying, 'COMPLETED'::character varying, 'CANCELLED'::character varying, 'RESCHEDULED'::character varying])::text[])))
);


ALTER TABLE public.service_schedules OWNER TO postgres;

--
-- Name: society; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.society (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(255) NOT NULL,
    address text NOT NULL,
    city character varying(100) NOT NULL,
    state character varying(100) NOT NULL,
    pincode character varying(10) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    requested_by uuid,
    approved_by uuid,
    status character varying(20) DEFAULT 'PENDING'::character varying,
    approved_at timestamp without time zone
);


ALTER TABLE public.society OWNER TO postgres;

--
-- Name: user_otps; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_otps (
    id uuid NOT NULL,
    user_id uuid,
    otp_code character varying(10) NOT NULL,
    expires_at timestamp without time zone NOT NULL,
    is_used boolean DEFAULT false,
    attempts integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.user_otps OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id uuid NOT NULL,
    email character varying(255) NOT NULL,
    phone character varying(15) NOT NULL,
    password_hash character varying(255) NOT NULL,
    role character varying(255) NOT NULL,
    is_active boolean DEFAULT true,
    email_verified boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT role_check CHECK (((role)::text = ANY ((ARRAY['RESIDENT'::character varying, 'SERVICE_PROVIDER'::character varying, 'ADMIN'::character varying, 'PLATFORM_ADMIN'::character varying])::text[]))),
    CONSTRAINT users_role_check CHECK (((role)::text = ANY ((ARRAY['RESIDENT'::character varying, 'SERVICE_PROVIDER'::character varying, 'ADMIN'::character varying, 'PLATFORM_ADMIN'::character varying])::text[])))
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Data for Name: admin_profiles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.admin_profiles (user_id, first_name, last_name, society_id, created_at) FROM stdin;
33e6a890-bf90-4cee-8fc7-d8f735d74367	Babita	Gupta	744d5e03-18ba-46d9-b23a-2aaea5da1acb	2025-06-13 02:58:56.209277
7ef7f10c-92a7-4cd6-9b72-c2694fd7df87	Babita	Gupta	f88c3295-4b7d-401b-9f63-31e463c14046	2025-06-05 02:33:49.356209
380531f0-2b77-4454-80fc-7df075713750	Aman	Repuria	1b787fd8-d04b-4066-9aa5-f2a3624717f5	2025-06-13 18:40:19.066093
31f7c81a-d21e-4b9f-ad1e-67a606d49459	Atmaram 	Bhide	5017edf4-5a1c-4810-925f-c9a6e89bbc69	2025-06-20 04:34:06.660623
\.


--
-- Data for Name: platform_admin_profiles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.platform_admin_profiles (user_id, first_name, last_name, created_at) FROM stdin;
33e6a890-bf90-4cee-8fc7-d8f735d74367	BOSxsS3	Gupta	2025-06-04 21:23:09.478928
1985222c-0209-4bb0-8c64-e3942a98db36	Sonam	Gupta	2025-06-11 13:40:28.623916
cbccfc28-9bc6-4f82-9463-b90a658104e7	Raj	Gupta	2025-06-12 21:08:15.572904
\.


--
-- Data for Name: provider_responses; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.provider_responses (id, request_id, response, provider_id, total_cost, notes, responded_at) FROM stdin;
b3a3a2a5-ea6c-44cc-93e1-ff9e82ec1ecb	75bfb058-556a-405b-8639-ed4c98c4fa78	OUT_FOR_SERVICE	dabba96f-7002-4cec-9a5d-d40f312d6baf	\N	i'm aout for service	2025-06-27 14:06:20.810523
651bcde7-bf86-41df-9c6f-8c359e9d52de	6628e885-4bbf-4b6d-a4da-8509df90f92b	ACCEPTED	b42319c0-2d18-495d-b039-4c05eb262def	\N		2025-06-27 16:13:54.219717
ff12aaa3-7ab9-46fa-8991-7790e4007b5f	9946768f-ff8d-4f94-bd17-a0b45245a691	REJECTED	b42319c0-2d18-495d-b039-4c05eb262def	\N		2025-06-27 16:10:50.149698
3a6d32d0-1135-435e-8ef0-52b5c255f4b6	1e14eec9-338a-44c4-bcb4-25a551a0ac86	ACCEPTED	6af7e73e-65bd-4c86-b093-df4c473341d5	\N	Sham tk aaata hu	2025-06-28 02:59:49.415673
\.


--
-- Data for Name: request_media; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.request_media (id, request_id, url, filename, file_size, media_type, mime_type, is_before_service, uploaded_by, uploaded_by_type, created_at) FROM stdin;
da273dfd-a722-43ca-ad5d-3560e693449a	a64bcb49-a110-4c61-b3a4-18ad656a84b2	https://homehubbucket.s3.ap-south-1.amazonaws.com/requests/a64bcb49-a110-4c61-b3a4-18ad656a84b2/4fbc017b-341e-4206-83d1-2b0fcd3e2ade_banner.jpeg	banner.jpeg	263439	IMAGE	image/jpeg	\N	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	2025-06-17 22:37:52.29373
537eb3a1-3b59-4ff7-8c0c-7f07e212fb02	f1ce3129-0568-4e44-a177-59e3ba04ca63	https://homehubbucket.s3.ap-south-1.amazonaws.com/requests/f1ce3129-0568-4e44-a177-59e3ba04ca63/8acee78a-bc39-452d-99d2-85f8a3c1d902_banner.jpeg	banner.jpeg	263439	IMAGE	image/jpeg	\N	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	2025-06-17 22:59:50.366458
04a3b9a9-c136-49d3-a774-5876f7766dc9	aa99ab91-0c75-4463-815e-f233a8eb2e4f	requests/aa99ab91-0c75-4463-815e-f233a8eb2e4f/47c88855-da24-4fdc-a75f-ba160b5a9abb_banner.jpeg	banner.jpeg	263439	IMAGE	image/jpeg	\N	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	2025-06-17 23:03:16.491436
270fea80-54a9-4a39-aec5-4fc0aa5766fc	48abf537-7ede-4ca2-ad5d-17ee9185c1b2	requests/48abf537-7ede-4ca2-ad5d-17ee9185c1b2/98dd8e61-b5d3-4366-9f66-6dfb70416260_OIDL250604050134871293.pdf	OIDL250604050134871293.pdf	89560	DOCUMENT	application/pdf	\N	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	2025-06-18 00:16:35.915402
6445828f-385c-4246-93f2-92a83cb9af26	836ba55c-b43b-4797-aefd-952db38744f4	requests/836ba55c-b43b-4797-aefd-952db38744f4/2e5c05ae-fb71-4529-a372-f1bcb55b0ddb_OIDL250604050134871293.pdf	OIDL250604050134871293.pdf	89560	DOCUMENT	application/pdf	\N	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	2025-06-18 00:24:05.593631
e8ac8ac6-0fe0-41f5-bc80-8bd90680d86a	47d25a7f-2736-405c-945e-f21c801e2fbc	requests/47d25a7f-2736-405c-945e-f21c801e2fbc/1c121e42-d5ef-48d7-affb-571bbcc1d207_OIDL250604050134871293.pdf	OIDL250604050134871293.pdf	89560	DOCUMENT	application/pdf	\N	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	2025-06-18 00:25:54.751976
2b661b79-e731-4ec9-86c7-d7dc66ca7d10	ceb65970-36d6-473f-8cbd-14015140906f	requests/ceb65970-36d6-473f-8cbd-14015140906f/d01a9831-6236-49bb-b4e4-03f428ad2e03_PDF-REPORTS_20250604_SL_180681662_C15_GUPTA_cc8426_20250604152203_F.pdf	PDF-REPORTS_20250604_SL_180681662_C15_GUPTA_cc8426_20250604152203_F.pdf	237362	DOCUMENT	application/pdf	\N	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	2025-06-18 00:36:11.382602
00a510a0-44f4-4113-8381-df4539b015c5	ceb65970-36d6-473f-8cbd-14015140906f	requests/ceb65970-36d6-473f-8cbd-14015140906f/5dedcab8-f001-4635-97da-bd5b45452a82_OIDL250604050134871293.pdf	OIDL250604050134871293.pdf	89560	DOCUMENT	application/pdf	\N	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	2025-06-18 00:36:11.399603
f85c7d6c-3aa5-4bc3-a984-301a2729cbda	2b98e88a-9eaa-4463-a5db-762ba2784c44	requests/2b98e88a-9eaa-4463-a5db-762ba2784c44/09d1a1d8-450e-42be-8b96-5ec0693967ac_AYUSH_RESUME.pdf	AYUSH_RESUME.pdf	210590	DOCUMENT	application/pdf	\N	726b62fe-3dc5-45da-8490-e0d2f7f06715	RESIDENT	2025-06-20 04:39:34.473536
f23e2fa0-b75a-4125-8daf-882f4fc07efa	6628e885-4bbf-4b6d-a4da-8509df90f92b	requests/6628e885-4bbf-4b6d-a4da-8509df90f92b/7905c04c-670b-4814-92cf-da522802d05d_SET25280119342218190011670_.pdf	SET25280119342218190011670_.pdf	180868	DOCUMENT	application/pdf	\N	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	2025-06-26 23:40:12.700339
da293929-9a55-437b-9dd9-12a9166debc7	1e14eec9-338a-44c4-bcb4-25a551a0ac86	requests/1e14eec9-338a-44c4-bcb4-25a551a0ac86/1d5ad4e3-2254-418c-9d68-9a8a3cb94335_OIDL250604050134871293.pdf	OIDL250604050134871293.pdf	89560	DOCUMENT	application/pdf	\N	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	2025-06-27 00:04:39.515925
8e82136c-ae31-4987-bf9e-aa98a068f85c	9946768f-ff8d-4f94-bd17-a0b45245a691	requests/9946768f-ff8d-4f94-bd17-a0b45245a691/ed5eeb0a-af42-4e87-b992-f23f88c45e3f_AYUSH_RESUME.pdf	AYUSH_RESUME.pdf	210503	DOCUMENT	application/pdf	\N	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	2025-06-27 00:16:09.586755
642cd1e7-ff6a-4946-a03b-965920a1fae2	902c3342-3143-4eef-a835-9040ec549a92	requests/902c3342-3143-4eef-a835-9040ec549a92/eaab9fc4-8888-422c-bf28-33f8f719c2d5_SET25280119342218190011670_.pdf	SET25280119342218190011670_.pdf	180868	DOCUMENT	application/pdf	\N	ecae01be-1bf7-4397-9bfc-0f427a470a2f	RESIDENT	2025-06-27 00:18:06.452416
02bc4335-2920-4c1b-9d20-7312471859d7	c913674e-7e74-49e7-a794-db3289ab474d	requests/c913674e-7e74-49e7-a794-db3289ab474d/d0488aaf-c5b6-4a91-bafe-aca60f51351a_lec14Notes.pdf	lec14Notes.pdf	472967	DOCUMENT	application/pdf	\N	d0b0cbe2-c960-4e1d-8571-eae248ca9e60	RESIDENT	2025-06-27 12:21:40.707819
\.


--
-- Data for Name: request_status_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.request_status_history (id, request_id, from_status, to_status, changed_by, changed_by_type, reason, notes, created_at) FROM stdin;
6ae736ac-19cd-4e2e-af0d-cfa0587bb685	a64bcb49-a110-4c61-b3a4-18ad656a84b2	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-17 22:37:52.304457
f5523df1-f38e-4e7a-8ce8-0679650316be	f1ce3129-0568-4e44-a177-59e3ba04ca63	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-17 22:59:50.372462
fddd6482-c0e9-49fd-a0b8-4353bc41b17d	aa99ab91-0c75-4463-815e-f233a8eb2e4f	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-17 23:03:16.501494
bd70be90-15d3-4f90-99a6-442bb31ac3af	48abf537-7ede-4ca2-ad5d-17ee9185c1b2	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-18 00:16:35.920404
55b84ce6-eb52-489b-b099-1d8cc6ba0779	836ba55c-b43b-4797-aefd-952db38744f4	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-18 00:24:05.596628
9a124a81-1b22-45c8-860c-aa4dc259ce86	47d25a7f-2736-405c-945e-f21c801e2fbc	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-18 00:25:54.752975
f6d75e08-7117-4954-a421-7b461be6d0e7	ceb65970-36d6-473f-8cbd-14015140906f	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-18 00:36:11.402602
2162c9f7-24d8-432b-9126-61d38638b705	2b98e88a-9eaa-4463-a5db-762ba2784c44	\N	SUBMITTED	726b62fe-3dc5-45da-8490-e0d2f7f06715	RESIDENT	Request Created	\N	2025-06-20 04:39:34.48654
d4ee020c-8b18-4ff4-a12b-ce46b29721f9	51d40bc4-82d6-466b-8436-12ff3e358a7f	\N	SUBMITTED	726b62fe-3dc5-45da-8490-e0d2f7f06715	RESIDENT	Request Created	\N	2025-06-20 05:16:23.068277
270881a5-0a0b-4f95-b7b2-74823be88c7d	4a0e2788-2535-4fb8-9f7c-750ffb6fc5ac	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-20 23:47:52.659663
fff294f5-6629-4862-bab5-3832177ece7a	091dd912-ed00-4b97-863a-00cec959fbcd	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-20 23:49:01.31066
d01c1db4-8c5d-466a-945b-7f4bddb46b96	dbd5e28e-50ee-4e0b-b35b-bcfd83d27b87	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-20 23:49:37.793143
4772f6c1-9ebb-49f1-b9df-c8763fe1db83	e161eabe-9d7f-4d54-835a-2febec829d7c	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-21 00:09:28.178368
5ad76528-cb4a-4d64-8e8d-49d575a16344	cd94f194-6b84-4f2c-a1e4-9ecc9b0e3e94	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-21 01:09:34.281031
b150aad5-dd89-48b1-b222-9a3768b0c279	c86b0906-8b2c-4686-a385-64f31feccd87	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-21 01:17:19.258482
bc4a305d-5a8d-480c-86ce-8333ca4ecfd5	ce76a7ba-8d47-46bb-b545-e3f328151caf	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-21 01:18:56.841401
a4c9bae9-1eb9-4f19-845b-17d1afc9dabd	0d483998-875d-4e64-af2f-9061672f3bc2	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-21 01:19:22.187187
0d531820-347a-4780-b16e-e347f1ff35d6	44adbd62-3d75-45f2-8d76-c956cc49805c	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-21 01:22:19.427828
54586b2c-6cb2-46ac-8307-0002d3e529d4	9b72b1c2-8647-4826-9bf9-b40989e0a096	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-21 01:27:28.587427
92e7b212-541f-47d7-b5a7-36059f0c1898	2a069b87-fbb5-4a7a-b382-553fa9f1392b	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-25 13:32:22.081314
b06598d3-7ff7-441f-b5cf-c4d412ec9227	3d7ec344-4b2f-4664-86ad-b09ae134733f	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-26 23:39:20.998158
ddced3f8-f913-4898-96b1-d11c9460cee9	6628e885-4bbf-4b6d-a4da-8509df90f92b	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-26 23:40:12.71234
14996dad-9bdc-42e4-8e34-ad57695e7bb5	b63a1ac3-08fe-44db-9d9d-79eb4d63709b	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-27 00:01:38.270983
a12e6d3f-78e6-4cea-9e3f-0c26d91bc423	1e14eec9-338a-44c4-bcb4-25a551a0ac86	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-27 00:04:39.519913
a36a5801-9cc4-45b7-8bbc-077dce524493	9946768f-ff8d-4f94-bd17-a0b45245a691	\N	SUBMITTED	65b9ecc9-6908-454b-8965-41c28228ad03	RESIDENT	Request Created	\N	2025-06-27 00:16:09.589759
ba173521-789b-41b8-a641-1d028c4c5b87	902c3342-3143-4eef-a835-9040ec549a92	\N	SUBMITTED	ecae01be-1bf7-4397-9bfc-0f427a470a2f	RESIDENT	Request Created	\N	2025-06-27 00:18:06.454424
67757580-0891-4f6e-b727-e3870e50b351	c913674e-7e74-49e7-a794-db3289ab474d	\N	SUBMITTED	d0b0cbe2-c960-4e1d-8571-eae248ca9e60	RESIDENT	Request Created	\N	2025-06-27 12:21:40.7098
ceabe9b9-1ddc-47bb-bbf1-bfbfac6fb866	75bfb058-556a-405b-8639-ed4c98c4fa78	\N	SUBMITTED	d0b0cbe2-c960-4e1d-8571-eae248ca9e60	RESIDENT	Request Created	\N	2025-06-27 12:27:24.112621
46600cd8-5e8f-42d4-b2ee-16d7082413f0	75bfb058-556a-405b-8639-ed4c98c4fa78	REJECTED	SCHEDULED	dabba96f-7002-4cec-9a5d-d40f312d6baf	PROVIDER	\N	I'll handle this request personally	2025-06-27 14:48:18.14207
b6fb6d7c-6b73-4779-bd17-d5c9bd9dfca0	75bfb058-556a-405b-8639-ed4c98c4fa78	SCHEDULED	REJECTED	dabba96f-7002-4cec-9a5d-d40f312d6baf	PROVIDER	\N	i'm aout for service	2025-06-27 15:09:10.168976
0799614e-33a8-4505-8537-3f63d7fb5a9f	75bfb058-556a-405b-8639-ed4c98c4fa78	REJECTED	IN_PROGRESS	dabba96f-7002-4cec-9a5d-d40f312d6baf	PROVIDER	\N	i'm aout for service	2025-06-27 15:17:44.850688
8127a384-85ab-42a7-8867-3e8db94b6ac5	9946768f-ff8d-4f94-bd17-a0b45245a691	SUBMITTED	SCHEDULED	b42319c0-2d18-495d-b039-4c05eb262def	PROVIDER	\N	ok	2025-06-27 16:10:50.174695
2f5e5363-3add-451d-895e-7caf2a400850	6628e885-4bbf-4b6d-a4da-8509df90f92b	SUBMITTED	SCHEDULED	b42319c0-2d18-495d-b039-4c05eb262def	PROVIDER	\N		2025-06-27 16:13:54.222724
668367c2-ade1-4ec9-90fe-5cb9bc8e16ea	9946768f-ff8d-4f94-bd17-a0b45245a691	SCHEDULED	IN_PROGRESS	b42319c0-2d18-495d-b039-4c05eb262def	PROVIDER	\N		2025-06-27 16:35:42.759129
66ab66ff-dcc9-48e9-bd1e-326fe4aca9ca	9946768f-ff8d-4f94-bd17-a0b45245a691	IN_PROGRESS	REJECTED	b42319c0-2d18-495d-b039-4c05eb262def	PROVIDER	\N		2025-06-27 16:38:40.605938
ce14bb03-58ff-405b-97f0-44ed9cfba345	1e14eec9-338a-44c4-bcb4-25a551a0ac86	SUBMITTED	SCHEDULED	6af7e73e-65bd-4c86-b093-df4c473341d5	PROVIDER	\N	Sham tk aaata hu	2025-06-28 02:59:49.465837
\.


--
-- Data for Name: resident_profiles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.resident_profiles (user_id, first_name, last_name, apartment_number, society_id, emergency_contact, created_at) FROM stdin;
ecae01be-1bf7-4397-9bfc-0f427a470a2f	Abhinav	BC	das	f88c3295-4b7d-401b-9f63-31e463c14046	\N	2025-06-10 22:00:21.871966
d0b0cbe2-c960-4e1d-8571-eae248ca9e60	IShit	Gupta	12	f88c3295-4b7d-401b-9f63-31e463c14046	\N	2025-06-11 14:07:07.066105
fce3e272-9cdc-45a5-abd0-77e25b729932	Ankit	Gupta	78A	1b787fd8-d04b-4066-9aa5-f2a3624717f5	\N	2025-06-14 15:56:55.864649
65b9ecc9-6908-454b-8965-41c28228ad03	IShit	Gupta	12	1b787fd8-d04b-4066-9aa5-f2a3624717f5	\N	2025-06-16 03:45:47.291364
726b62fe-3dc5-45da-8490-e0d2f7f06715	Shreya	Lalit	g	5017edf4-5a1c-4810-925f-c9a6e89bbc69	\N	2025-06-20 04:38:42.726545
\.


--
-- Data for Name: service_categories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.service_categories (id, name, description, is_active, created_at) FROM stdin;
e31dc8c4-d6b6-49e4-893f-12396d3ceb62	PLUMBING	Plumbing and water-related services	t	2025-06-01 00:37:33.724868
4f183ab7-836b-4831-8ad2-5ac050e11a78	ELECTRICAL	Electrical work and repairs	t	2025-06-01 00:37:33.724868
67af6a4b-b514-4ca8-a444-431f205f3bce	CLEANING	Home and office cleaning services	t	2025-06-01 00:37:33.724868
91a324f4-feec-40a5-bf65-7c6465d69fef	CARPENTRY	Furniture and woodwork services	t	2025-06-01 00:37:33.724868
b1339fbd-679f-4510-8a02-e35f13ae4999	PAINTING	Interior and exterior painting	t	2025-06-01 00:37:33.724868
6113137b-dec1-44d0-95ab-12167665342e	APPLIANCE_REPAIR	Home appliance repair and maintenance	t	2025-06-01 00:37:33.724868
e92b7f9e-8348-48dc-85ce-22326874de26	PEST_CONTROL	Pest control and fumigation	t	2025-06-01 00:37:33.724868
950ca9fb-c37f-482c-8b4f-a601a6ec8182	GARDENING	Landscaping and gardening services	t	2025-06-01 00:37:33.724868
36076f1d-4d75-45dd-9c54-62a5be571908	SECURITY	Security guard and surveillance services	t	2025-06-01 00:37:33.724868
468346bf-1225-419c-9e25-1051dd969de6	DELIVERY	Delivery and courier services	t	2025-06-01 00:37:33.724868
2a68c63e-b560-4321-83c0-7a85a290b9a9	Bakchodi	maza aayega	t	\N
ec204a69-fbb4-4f2d-9425-d79327aba7f7	Poo	maza aayega	t	2025-06-01 16:17:44.125898
980b285b-e336-4542-bfd5-800e426c64db	KuchuPuchu	maza aayega	t	2025-06-05 00:00:00
\.


--
-- Data for Name: service_provider_categories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.service_provider_categories (id, service_provider_id, category_id, hourly_rate, min_charge, is_primary, created_at) FROM stdin;
6bba0c60-9191-4a01-9b55-f6dc6432c0cc	b42319c0-2d18-495d-b039-4c05eb262def	ec204a69-fbb4-4f2d-9425-d79327aba7f7	300	200	f	2025-06-06 00:20:09.055169
75caf442-e657-41e8-ad80-539b79aae3ae	b42319c0-2d18-495d-b039-4c05eb262def	980b285b-e336-4542-bfd5-800e426c64db	200	200	f	2025-06-06 00:23:39.379837
5e61cad5-dc11-4440-a9e2-8d058895efc0	b42319c0-2d18-495d-b039-4c05eb262def	e92b7f9e-8348-48dc-85ce-22326874de26	200	200	f	2025-06-07 12:32:13.45435
969adac3-0e23-46e1-82b1-df254caa54c3	b42319c0-2d18-495d-b039-4c05eb262def	6113137b-dec1-44d0-95ab-12167665342e	2	200	f	2025-06-06 01:01:35.360813
94a1b0f0-194b-4d16-8cc5-8d876e8e3458	6af7e73e-65bd-4c86-b093-df4c473341d5	e92b7f9e-8348-48dc-85ce-22326874de26	200	200	f	2025-06-11 00:15:27.419502
0d3d257c-3689-4e5f-8264-d951e9fd2a11	dabba96f-7002-4cec-9a5d-d40f312d6baf	e92b7f9e-8348-48dc-85ce-22326874de26	200	200	f	2025-06-11 01:18:52.352725
e45867d3-1ec4-488d-8de5-4ab0992a5663	dabba96f-7002-4cec-9a5d-d40f312d6baf	b1339fbd-679f-4510-8a02-e35f13ae4999	200	200	f	2025-06-11 02:14:39.708674
d4b88cdc-694e-4168-9230-d9fbf2e66f62	dabba96f-7002-4cec-9a5d-d40f312d6baf	950ca9fb-c37f-482c-8b4f-a601a6ec8182	200	200	t	2025-06-11 02:20:27.636204
d1fba3d2-2f6c-4f86-8d94-fbe83d1e1c87	d5362e83-17c6-46d7-9464-3838cf983e42	950ca9fb-c37f-482c-8b4f-a601a6ec8182	200	200	t	2025-06-15 20:50:09.432932
2fcb756f-4ca6-46fc-beba-b5f1607f8045	d5362e83-17c6-46d7-9464-3838cf983e42	4f183ab7-836b-4831-8ad2-5ac050e11a78	200	200	t	2025-06-15 21:03:01.155455
adff91cb-0643-41db-827b-5a3dc9ca4bb1	d5362e83-17c6-46d7-9464-3838cf983e42	2a68c63e-b560-4321-83c0-7a85a290b9a9	453	54	f	2025-06-15 22:31:10.659584
78c6eb5e-edd2-4573-ba7c-9dd984a364c0	d5362e83-17c6-46d7-9464-3838cf983e42	6113137b-dec1-44d0-95ab-12167665342e	537	6998	t	2025-06-15 22:32:38.598197
31968371-2111-4e5b-8793-8074ed04468d	b42319c0-2d18-495d-b039-4c05eb262def	4f183ab7-836b-4831-8ad2-5ac050e11a78	45	10	f	2025-06-20 05:19:19.592279
\.


--
-- Data for Name: service_provider_profiles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.service_provider_profiles (user_id, first_name, last_name, business_name, description, experience_years, is_verified, verification_date, rating, total_jobs_completed, base_service_charge, phone_secondary, address, city, state, pincode, available_hours_start, available_hours_end, is_available, created_at, updated_at) FROM stdin;
b42319c0-2d18-495d-b039-4c05eb262def	sHUBHAS	mISTRI	sHUBHAS BIJLI WALA	sb aata hai.	10	f	\N	0.00	0	500	9123456789	123 Main Street, Sector 22	Gurgaon	Haryana	122001	09:00:00	18:00:00	t	2024-12-01 09:00:00	2024-12-05 14:30:00
6af7e73e-65bd-4c86-b093-df4c473341d5	Pooja	Bhatia	sHUBHAS BIJLI WALA	sb aata hai.	10	f	\N	0.00	0	500	9123456789	123 Main Street, Sector 22	Gurgaon	Haryana	122001	09:00:00	18:00:00	t	2025-06-07 15:52:07.50844	2025-06-07 15:52:07.50844
dabba96f-7002-4cec-9a5d-d40f312d6baf	Aditya	Gupta	sHUBHAS BIJLI WALA	sb aata hai.	10	f	\N	0.00	0	500	9123456789	123 Main Street, Sector 22	Gurgaon	Haryana	122001	09:00:00	18:00:00	t	2025-06-11 01:18:16.294899	2025-06-11 01:18:16.294899
6a95473a-c736-4539-81c0-67241fabede4	Suresh	Gupta	Suresh Plumber 	sb aata hai.	10	f	\N	0.00	0	500	9123456789	123 Main Street, Sector 22	Gurgaon	Haryana	122001	09:00:00	18:00:00	t	2025-06-15 13:21:19.386869	2025-06-15 13:21:19.386869
d5362e83-17c6-46d7-9464-3838cf983e42	Mukesh	Bhatia	Mukesh ELectricals	gfdhgfd	1	f	\N	2.00	0	200	2445	Chaupal Nagar,Pitampura	Surat	Gujarat	110034	\N	\N	t	2025-06-15 19:10:32.881885	2025-06-15 19:10:32.881885
\.


--
-- Data for Name: service_provider_societies; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.service_provider_societies (id, service_provider_id, society_id, is_preferred, approval_status, approved_by, approved_at, created_at) FROM stdin;
b0eb66b3-a5ed-4330-bf02-eead792bc094	b42319c0-2d18-495d-b039-4c05eb262def	744d5e03-18ba-46d9-b23a-2aaea5da1acb	f	PENDING	\N	\N	2025-06-07 13:01:05.513305
9e06b5db-a4ca-4066-ba82-f8b562c7f257	6af7e73e-65bd-4c86-b093-df4c473341d5	744d5e03-18ba-46d9-b23a-2aaea5da1acb	f	APPROVED	7ef7f10c-92a7-4cd6-9b72-c2694fd7df87	2025-06-07 16:50:04.009123	2025-06-07 15:52:31.991364
ed3634d3-01b1-4b88-b793-cc31133c5b16	dabba96f-7002-4cec-9a5d-d40f312d6baf	f88c3295-4b7d-401b-9f63-31e463c14046	f	APPROVED	7ef7f10c-92a7-4cd6-9b72-c2694fd7df87	2025-06-11 01:22:30.17934	2025-06-11 01:20:36.174457
c88b8310-92c7-4667-ba55-1d34630e2aae	b42319c0-2d18-495d-b039-4c05eb262def	1b787fd8-d04b-4066-9aa5-f2a3624717f5	f	APPROVED	380531f0-2b77-4454-80fc-7df075713750	2025-06-13 22:23:17.814768	2025-06-13 22:09:01.55636
ea59599c-bfba-46ba-9d27-d96925102341	6af7e73e-65bd-4c86-b093-df4c473341d5	1b787fd8-d04b-4066-9aa5-f2a3624717f5	f	APPROVED	380531f0-2b77-4454-80fc-7df075713750	2025-06-14 16:33:12.254993	2025-06-14 15:50:24.300735
39f63641-a199-407d-8f4a-f8b4b2a0be55	dabba96f-7002-4cec-9a5d-d40f312d6baf	1b787fd8-d04b-4066-9aa5-f2a3624717f5	f	APPROVED	380531f0-2b77-4454-80fc-7df075713750	2025-06-14 23:34:20.891251	2025-06-14 15:29:38.774359
4e8cbd28-b27a-4e49-8826-a29488de73e7	6a95473a-c736-4539-81c0-67241fabede4	1b787fd8-d04b-4066-9aa5-f2a3624717f5	f	APPROVED	380531f0-2b77-4454-80fc-7df075713750	2025-06-15 13:29:46.532115	2025-06-15 13:29:33.950561
28a55876-7396-4856-aa17-40e79b1b6b06	d5362e83-17c6-46d7-9464-3838cf983e42	1b787fd8-d04b-4066-9aa5-f2a3624717f5	f	APPROVED	380531f0-2b77-4454-80fc-7df075713750	2025-06-16 02:48:24.855247	2025-06-16 02:42:41.297923
a6e1c154-8bcb-4cec-a4c0-a8867f3f2d13	d5362e83-17c6-46d7-9464-3838cf983e42	8fe85cad-8829-4213-9fb7-eec27e1c781e	f	PENDING	\N	\N	2025-06-16 02:55:05.770159
4bb6974e-26c1-4e20-9b49-e36d0ae34dd6	b42319c0-2d18-495d-b039-4c05eb262def	5017edf4-5a1c-4810-925f-c9a6e89bbc69	f	APPROVED	31f7c81a-d21e-4b9f-ad1e-67a606d49459	2025-06-20 04:35:51.062114	2025-06-20 04:34:31.426053
08201f7e-0ef7-4675-bfc5-9a394ccb952e	6af7e73e-65bd-4c86-b093-df4c473341d5	5017edf4-5a1c-4810-925f-c9a6e89bbc69	f	PENDING	\N	\N	2025-06-28 03:08:52.070571
\.


--
-- Data for Name: service_ratings; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.service_ratings (id, request_id, resident_id, provider_id, overall_rating, quality_rating, timeliness_rating, professionalism_rating, value_rating, feedback, would_recommend, service_completed_on_time, created_at) FROM stdin;
\.


--
-- Data for Name: service_requests; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.service_requests (id, resident_id, provider_id, society_id, category_id, description, urgency, preferred_date, preferred_time_slot, location_details, contact_phone, status, final_cost, payment_method, payment_status, expires_at, created_at, updated_at) FROM stdin;
a64bcb49-a110-4c61-b3a4-18ad656a84b2	65b9ecc9-6908-454b-8965-41c28228ad03	b42319c0-2d18-495d-b039-4c05eb262def	1b787fd8-d04b-4066-9aa5-f2a3624717f5	6113137b-dec1-44d0-95ab-12167665342e	Kitchen faucet has been dripping constantly	MEDIUM	2025-06-20	Morning	Apartment 5B, Building 2	+919876543210	SUBMITTED	\N	\N	\N	\N	2025-06-17 22:37:52.228395	2025-06-17 22:37:52.228395
f1ce3129-0568-4e44-a177-59e3ba04ca63	65b9ecc9-6908-454b-8965-41c28228ad03	b42319c0-2d18-495d-b039-4c05eb262def	1b787fd8-d04b-4066-9aa5-f2a3624717f5	6113137b-dec1-44d0-95ab-12167665342e	Kitchen faucet has been dripping constantly	MEDIUM	2025-06-20	Morning	Apartment 5B, Building 2	+919876543210	SUBMITTED	\N	\N	\N	\N	2025-06-17 22:59:50.359076	2025-06-17 22:59:50.359076
aa99ab91-0c75-4463-815e-f233a8eb2e4f	65b9ecc9-6908-454b-8965-41c28228ad03	b42319c0-2d18-495d-b039-4c05eb262def	1b787fd8-d04b-4066-9aa5-f2a3624717f5	6113137b-dec1-44d0-95ab-12167665342e	Kitchen faucet has been dripping constantly	MEDIUM	2025-06-20	Morning	Apartment 5B, Building 2	+919876543210	SUBMITTED	\N	\N	\N	\N	2025-06-17 23:03:16.407477	2025-06-17 23:03:16.407477
48abf537-7ede-4ca2-ad5d-17ee9185c1b2	65b9ecc9-6908-454b-8965-41c28228ad03	d5362e83-17c6-46d7-9464-3838cf983e42	1b787fd8-d04b-4066-9aa5-f2a3624717f5	950ca9fb-c37f-482c-8b4f-a601a6ec8182	Ghas kaat	MEDIUM	\N		12 IOCL Society	6545987654	SUBMITTED	\N	\N	\N	\N	2025-06-18 00:16:35.902398	2025-06-18 00:16:35.902398
836ba55c-b43b-4797-aefd-952db38744f4	65b9ecc9-6908-454b-8965-41c28228ad03	b42319c0-2d18-495d-b039-4c05eb262def	1b787fd8-d04b-4066-9aa5-f2a3624717f5	ec204a69-fbb4-4f2d-9425-d79327aba7f7	dfd	MEDIUM	2025-06-27		12 IOCL Society	6545987654	SUBMITTED	\N	\N	\N	\N	2025-06-18 00:24:05.576629	2025-06-18 00:24:05.576629
47d25a7f-2736-405c-945e-f21c801e2fbc	65b9ecc9-6908-454b-8965-41c28228ad03	d5362e83-17c6-46d7-9464-3838cf983e42	1b787fd8-d04b-4066-9aa5-f2a3624717f5	950ca9fb-c37f-482c-8b4f-a601a6ec8182	dsad	MEDIUM	2025-06-14	Evening	12 IOCL Society	6545987654	SUBMITTED	\N	\N	\N	\N	2025-06-18 00:25:54.74898	2025-06-18 00:25:54.74898
ceb65970-36d6-473f-8cbd-14015140906f	65b9ecc9-6908-454b-8965-41c28228ad03	b42319c0-2d18-495d-b039-4c05eb262def	1b787fd8-d04b-4066-9aa5-f2a3624717f5	ec204a69-fbb4-4f2d-9425-d79327aba7f7	cdsc	MEDIUM	2025-06-07	Evening	12 IOCL Society	6545987654	SUBMITTED	\N	\N	\N	\N	2025-06-18 00:36:11.371605	2025-06-18 00:36:11.371605
2b98e88a-9eaa-4463-a5db-762ba2784c44	726b62fe-3dc5-45da-8490-e0d2f7f06715	b42319c0-2d18-495d-b039-4c05eb262def	5017edf4-5a1c-4810-925f-c9a6e89bbc69	980b285b-e336-4542-bfd5-800e426c64db	aa jao yaar jldi pyar do	MEDIUM	2025-07-05	Evening	g Bhatias Apartments	6545987654	SUBMITTED	\N	\N	\N	\N	2025-06-20 04:39:34.462558	2025-06-20 04:39:34.462558
51d40bc4-82d6-466b-8436-12ff3e358a7f	726b62fe-3dc5-45da-8490-e0d2f7f06715	b42319c0-2d18-495d-b039-4c05eb262def	5017edf4-5a1c-4810-925f-c9a6e89bbc69	ec204a69-fbb4-4f2d-9425-d79327aba7f7	fdsf	HIGH	2025-06-27	Afternoon	g Bhatias Apartments	6545987654	SUBMITTED	\N	\N	\N	\N	2025-06-20 05:16:23.06127	2025-06-20 05:16:23.06127
4a0e2788-2535-4fb8-9f7c-750ffb6fc5ac	65b9ecc9-6908-454b-8965-41c28228ad03	dabba96f-7002-4cec-9a5d-d40f312d6baf	1b787fd8-d04b-4066-9aa5-f2a3624717f5	b1339fbd-679f-4510-8a02-e35f13ae4999	jldi aa khooni pkdna hai	HIGH	2025-06-21	Evening	12 IOCL Society	6545987654	SUBMITTED	\N	\N	\N	\N	2025-06-20 23:47:52.609673	2025-06-20 23:47:52.609673
091dd912-ed00-4b97-863a-00cec959fbcd	65b9ecc9-6908-454b-8965-41c28228ad03	d5362e83-17c6-46d7-9464-3838cf983e42	1b787fd8-d04b-4066-9aa5-f2a3624717f5	950ca9fb-c37f-482c-8b4f-a601a6ec8182	fb	MEDIUM	2025-06-19	Evening	12 IOCL Society	6545987654	SUBMITTED	\N	\N	\N	\N	2025-06-20 23:49:01.306654	2025-06-20 23:49:01.306654
dbd5e28e-50ee-4e0b-b35b-bcfd83d27b87	65b9ecc9-6908-454b-8965-41c28228ad03	d5362e83-17c6-46d7-9464-3838cf983e42	1b787fd8-d04b-4066-9aa5-f2a3624717f5	2a68c63e-b560-4321-83c0-7a85a290b9a9	aaja fata fat	MEDIUM	2025-06-21	Evening	12 IOCL Society	6545987654	SUBMITTED	\N	\N	\N	\N	2025-06-20 23:49:37.791137	2025-06-20 23:49:37.791137
e161eabe-9d7f-4d54-835a-2febec829d7c	65b9ecc9-6908-454b-8965-41c28228ad03	d5362e83-17c6-46d7-9464-3838cf983e42	1b787fd8-d04b-4066-9aa5-f2a3624717f5	6113137b-dec1-44d0-95ab-12167665342e	cdsc	MEDIUM	2025-06-13	Evening	12 IOCL Society	6545987654	SUBMITTED	\N	\N	\N	\N	2025-06-21 00:09:28.167683	2025-06-21 00:09:28.167683
cd94f194-6b84-4f2c-a1e4-9ecc9b0e3e94	65b9ecc9-6908-454b-8965-41c28228ad03	d5362e83-17c6-46d7-9464-3838cf983e42	1b787fd8-d04b-4066-9aa5-f2a3624717f5	950ca9fb-c37f-482c-8b4f-a601a6ec8182	gfh	HIGH	2025-06-25	Afternoon	12 IOCL Society	6545987654	SUBMITTED	\N	\N	\N	\N	2025-06-21 01:09:34.246478	2025-06-21 01:09:34.246478
c86b0906-8b2c-4686-a385-64f31feccd87	65b9ecc9-6908-454b-8965-41c28228ad03	d5362e83-17c6-46d7-9464-3838cf983e42	1b787fd8-d04b-4066-9aa5-f2a3624717f5	950ca9fb-c37f-482c-8b4f-a601a6ec8182	gfdg	MEDIUM	2025-06-12	Evening	12 IOCL Society	6545987654	SUBMITTED	\N	\N	\N	\N	2025-06-21 01:17:19.252483	2025-06-21 01:17:19.252483
ce76a7ba-8d47-46bb-b545-e3f328151caf	65b9ecc9-6908-454b-8965-41c28228ad03	d5362e83-17c6-46d7-9464-3838cf983e42	1b787fd8-d04b-4066-9aa5-f2a3624717f5	950ca9fb-c37f-482c-8b4f-a601a6ec8182	dsa	MEDIUM	2025-06-28	Evening	12 IOCL Society	6545987654	SUBMITTED	\N	\N	\N	\N	2025-06-21 01:18:56.838397	2025-06-21 01:18:56.838397
0d483998-875d-4e64-af2f-9061672f3bc2	65b9ecc9-6908-454b-8965-41c28228ad03	d5362e83-17c6-46d7-9464-3838cf983e42	1b787fd8-d04b-4066-9aa5-f2a3624717f5	950ca9fb-c37f-482c-8b4f-a601a6ec8182	ds	MEDIUM	2025-06-14	Evening	12 IOCL Society	6545987654	SUBMITTED	\N	\N	\N	\N	2025-06-21 01:19:22.182181	2025-06-21 01:19:22.183184
44adbd62-3d75-45f2-8d76-c956cc49805c	65b9ecc9-6908-454b-8965-41c28228ad03	d5362e83-17c6-46d7-9464-3838cf983e42	1b787fd8-d04b-4066-9aa5-f2a3624717f5	950ca9fb-c37f-482c-8b4f-a601a6ec8182	ds	MEDIUM	2025-06-25	Evening	12 IOCL Society	6545987654	SUBMITTED	\N	\N	\N	\N	2025-06-21 01:22:19.423827	2025-06-21 01:22:19.423827
9b72b1c2-8647-4826-9bf9-b40989e0a096	65b9ecc9-6908-454b-8965-41c28228ad03	d5362e83-17c6-46d7-9464-3838cf983e42	1b787fd8-d04b-4066-9aa5-f2a3624717f5	950ca9fb-c37f-482c-8b4f-a601a6ec8182	bfd	MEDIUM	2025-06-11	Evening	12 IOCL Society	6545987654	SUBMITTED	\N	\N	\N	\N	2025-06-21 01:27:28.583375	2025-06-21 01:27:28.583375
2a069b87-fbb5-4a7a-b382-553fa9f1392b	65b9ecc9-6908-454b-8965-41c28228ad03	d5362e83-17c6-46d7-9464-3838cf983e42	1b787fd8-d04b-4066-9aa5-f2a3624717f5	950ca9fb-c37f-482c-8b4f-a601a6ec8182	dsf	MEDIUM	2025-06-28	Evening	12 IOCL Society	6545987654	SUBMITTED	\N	\N	\N	\N	2025-06-25 13:32:21.990768	2025-06-25 13:32:21.990768
3d7ec344-4b2f-4664-86ad-b09ae134733f	65b9ecc9-6908-454b-8965-41c28228ad03	b42319c0-2d18-495d-b039-4c05eb262def	1b787fd8-d04b-4066-9aa5-f2a3624717f5	ec204a69-fbb4-4f2d-9425-d79327aba7f7	ds	MEDIUM	2025-06-13	Evening	12 IOCL Society	6545987654	SUBMITTED	\N	\N	\N	\N	2025-06-26 23:39:20.913157	2025-06-26 23:39:20.913157
b63a1ac3-08fe-44db-9d9d-79eb4d63709b	65b9ecc9-6908-454b-8965-41c28228ad03	6af7e73e-65bd-4c86-b093-df4c473341d5	1b787fd8-d04b-4066-9aa5-f2a3624717f5	e92b7f9e-8348-48dc-85ce-22326874de26	jldi aa	MEDIUM	2025-06-29	Evening	12 IOCL Society	6545987654	SUBMITTED	\N	\N	\N	\N	2025-06-27 00:01:38.262984	2025-06-27 00:01:38.262984
902c3342-3143-4eef-a835-9040ec549a92	ecae01be-1bf7-4397-9bfc-0f427a470a2f	dabba96f-7002-4cec-9a5d-d40f312d6baf	f88c3295-4b7d-401b-9f63-31e463c14046	e92b7f9e-8348-48dc-85ce-22326874de26	csa	HIGH	2025-06-30	Evening	das Green Valley Residency	6545987654	SUBMITTED	\N	\N	\N	\N	2025-06-27 00:18:06.44741	2025-06-27 00:18:06.44741
c913674e-7e74-49e7-a794-db3289ab474d	d0b0cbe2-c960-4e1d-8571-eae248ca9e60	dabba96f-7002-4cec-9a5d-d40f312d6baf	f88c3295-4b7d-401b-9f63-31e463c14046	e92b7f9e-8348-48dc-85ce-22326874de26	My plants are getting damamged	HIGH	2025-06-29	Afternoon	12 Green Valley Residency	6545987654	SUBMITTED	\N	\N	\N	\N	2025-06-27 12:21:40.671961	2025-06-27 12:21:40.671961
75bfb058-556a-405b-8639-ed4c98c4fa78	d0b0cbe2-c960-4e1d-8571-eae248ca9e60	dabba96f-7002-4cec-9a5d-d40f312d6baf	f88c3295-4b7d-401b-9f63-31e463c14046	b1339fbd-679f-4510-8a02-e35f13ae4999	Wife painting	HIGH	2025-07-17	Afternoon	12 Green Valley Residency	6545987654	IN_PROGRESS	\N	\N	\N	\N	2025-06-27 12:27:24.106621	2025-06-27 15:17:44.855731
6628e885-4bbf-4b6d-a4da-8509df90f92b	65b9ecc9-6908-454b-8965-41c28228ad03	b42319c0-2d18-495d-b039-4c05eb262def	1b787fd8-d04b-4066-9aa5-f2a3624717f5	ec204a69-fbb4-4f2d-9425-d79327aba7f7	csacdsc	MEDIUM	2025-06-16	Evening	12 IOCL Society	6545987654	SCHEDULED	\N	\N	\N	\N	2025-06-26 23:40:12.683321	2025-06-27 16:13:54.225716
9946768f-ff8d-4f94-bd17-a0b45245a691	65b9ecc9-6908-454b-8965-41c28228ad03	b42319c0-2d18-495d-b039-4c05eb262def	1b787fd8-d04b-4066-9aa5-f2a3624717f5	4f183ab7-836b-4831-8ad2-5ac050e11a78	Cooler sh krna hai 	MEDIUM	2025-06-28	Evening	12 IOCL Society	6545987654	REJECTED	\N	\N	\N	\N	2025-06-27 00:16:09.581753	2025-06-27 16:38:40.622937
1e14eec9-338a-44c4-bcb4-25a551a0ac86	65b9ecc9-6908-454b-8965-41c28228ad03	6af7e73e-65bd-4c86-b093-df4c473341d5	1b787fd8-d04b-4066-9aa5-f2a3624717f5	e92b7f9e-8348-48dc-85ce-22326874de26	gfngjfgh	MEDIUM	2025-06-14	Evening	12 IOCL Society	6545987654	SCHEDULED	\N	\N	\N	\N	2025-06-27 00:04:39.506917	2025-06-28 02:59:49.469866
\.


--
-- Data for Name: service_schedules; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.service_schedules (id, request_id, provider_id, scheduled_date, estimated_start_time, estimated_end_time, actual_start_time, actual_end_time, status, reschedule_reason, created_at, updated_at) FROM stdin;
\.


--
-- Data for Name: society; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.society (id, name, address, city, state, pincode, created_at, requested_by, approved_by, status, approved_at) FROM stdin;
5017edf4-5a1c-4810-925f-c9a6e89bbc69	Bhatias Apartments	Srinagar	Gurgaon	Haryana	546541	2025-06-20 04:24:56.89288	31f7c81a-d21e-4b9f-ad1e-67a606d49459	cbccfc28-9bc6-4f82-9463-b90a658104e7	APPROVED	2025-06-20 04:26:40.215159
b0c5349f-59be-4a5d-875b-5af7b929c454	Bhatias Apartments	Srinagar	Gurgaon	Haryana	546541	2025-06-20 04:24:56.972894	31f7c81a-d21e-4b9f-ad1e-67a606d49459	cbccfc28-9bc6-4f82-9463-b90a658104e7	APPROVED	2025-06-20 04:28:46.775668
9a5d8286-812b-45aa-ae2e-b815531604f1	Hatiram \n	Madanpur	Firozabad	Uttar Pradesh	283203	2025-06-12 01:44:49.700883	5c1a68ad-ee3c-4b25-b730-7bf5751fda68	33e6a890-bf90-4cee-8fc7-d8f735d74367	APPROVED	2025-06-12 02:42:38.12391
f88c3295-4b7d-401b-9f63-31e463c14046	Green Valley Residency	Plot 24, Sector 10, near Central Park	Pune	Maharashtra	411045	2025-05-31 22:21:42.342232	7ef7f10c-92a7-4cd6-9b72-c2694fd7df87	33e6a890-bf90-4cee-8fc7-d8f735d74367	APPROVED	2025-06-13 13:18:12.671685
744d5e03-18ba-46d9-b23a-2aaea5da1acb	Gokuldham	Goregaon	Mumbai	Maharashtra	411445	2025-06-01 01:55:59.319316	7ef7f10c-92a7-4cd6-9b72-c2694fd7df87	1985222c-0209-4bb0-8c64-e3942a98db36	APPROVED	2025-06-12 02:42:38.12391
905fdcb9-e568-426c-b5e7-a968642bd49a	Neelam Society	10-A chuapal parking near Sbi tiger Barhiya	Jehanabad	Bihar	811311	2025-06-13 14:38:37.154463	0fc1f65a-a762-42a6-b97a-94c5b9f4780a	cbccfc28-9bc6-4f82-9463-b90a658104e7	APPROVED	2025-06-13 16:16:03.77318
3319dfd4-c5c9-45fd-b6af-6e94fd7b502d	Pooja apartments	Chaupal Nagar,Pitampura	South Delhi	Delhi	110034	2025-06-13 02:12:11.118097	9a870b03-4d06-4bdf-8426-7a1f9f975804	cbccfc28-9bc6-4f82-9463-b90a658104e7	APPROVED	2025-06-13 16:18:06.951088
5be2c0a8-2b3b-4dc8-b9f4-29ab8cf486e4	Pooja apartments	Chaupal Nagar,Pitampura	Shahdara	Delhi	110034	2025-06-13 01:54:48.064226	9a870b03-4d06-4bdf-8426-7a1f9f975804	cbccfc28-9bc6-4f82-9463-b90a658104e7	APPROVED	2025-06-13 16:19:30.758936
c7e03088-af9c-4ab0-91f7-a845d4f3ef9c	Pooja apartments	Chaupal Nagar,Pitampura	South East Delhi	Delhi	110034	2025-06-13 02:09:35.951161	9a870b03-4d06-4bdf-8426-7a1f9f975804	cbccfc28-9bc6-4f82-9463-b90a658104e7	APPROVED	2025-06-13 16:19:47.83636
48531090-93ce-4dfe-b789-fc8811bb0ea5	Pooja apartments	Chaupal Nagar,Pitampura	South Delhi	Delhi	110034	2025-06-13 02:07:31.220278	9a870b03-4d06-4bdf-8426-7a1f9f975804	cbccfc28-9bc6-4f82-9463-b90a658104e7	APPROVED	2025-06-13 16:21:23.338818
fe714641-3191-4276-99ef-33b4c7a28d91	Pooja apartments	Chaupal Nagar,Pitampura	South Delhi	Delhi	110034	2025-06-13 02:05:50.933683	9a870b03-4d06-4bdf-8426-7a1f9f975804	cbccfc28-9bc6-4f82-9463-b90a658104e7	APPROVED	2025-06-13 16:24:49.537497
d1741e5c-b5ec-46af-857f-774e7dbdfe8c	Pooja apartments	Chaupal Nagar,Pitampura	South Delhi	Delhi	110034	2025-06-13 02:05:48.786652	9a870b03-4d06-4bdf-8426-7a1f9f975804	cbccfc28-9bc6-4f82-9463-b90a658104e7	APPROVED	2025-06-13 16:25:43.523413
4234b8cf-ecc1-447d-9f38-3c163563c130	Pooja apartments	Chaupal Nagar,Pitampura	South West Delhi	Delhi	110034	2025-06-13 01:59:08.560563	9a870b03-4d06-4bdf-8426-7a1f9f975804	cbccfc28-9bc6-4f82-9463-b90a658104e7	APPROVED	2025-06-13 16:27:01.50304
1b787fd8-d04b-4066-9aa5-f2a3624717f5	IOCL Society	Sbi bank ke bagal wali gali 8/438 block 8 khichripur	Surat	Gujarat	454541	2025-06-13 18:31:44.58997	380531f0-2b77-4454-80fc-7df075713750	cbccfc28-9bc6-4f82-9463-b90a658104e7	APPROVED	2025-06-13 18:32:31.770207
8fe85cad-8829-4213-9fb7-eec27e1c781e	Sheela ki jawani	Madanpur	Surat	Gujarat	468694	2025-06-16 00:17:31.571558	5c1a68ad-ee3c-4b25-b730-7bf5751fda68	33e6a890-bf90-4cee-8fc7-d8f735d74367	APPROVED	2025-06-16 00:20:30.064592
17b90246-b418-4e4e-870f-78c66d79cd3e	Pooja apartments	Chaupal Nagar,Pitampura	South East Delhi	Delhi	110034	2025-06-13 01:57:24.196174	9a870b03-4d06-4bdf-8426-7a1f9f975804	cbccfc28-9bc6-4f82-9463-b90a658104e7	APPROVED	2025-06-16 03:14:21.260637
\.


--
-- Data for Name: user_otps; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_otps (id, user_id, otp_code, expires_at, is_used, attempts, created_at) FROM stdin;
25c08c28-bf85-4183-95a2-0a81fc7a14f6	ecae01be-1bf7-4397-9bfc-0f427a470a2f	545457	2025-06-10 22:03:23.650386	t	0	2025-06-10 21:58:23.653421
200a6314-a4ab-4fec-a272-8f00498e9e3c	dabba96f-7002-4cec-9a5d-d40f312d6baf	813582	2025-06-11 01:22:08.946968	t	0	2025-06-11 01:17:09.003137
dab0976b-3311-434b-91c8-6fc1df31318c	d0b0cbe2-c960-4e1d-8571-eae248ca9e60	578997	2025-06-11 14:09:45.304794	t	0	2025-06-11 14:04:45.392518
e8489035-e65e-4f48-83b8-ad49784f2212	4919d3f4-eef6-4c37-b358-7470434d175a	139702	2025-06-11 19:07:13.492006	f	0	2025-06-11 19:02:13.624393
949fe954-7e53-4c94-9212-e4e29aa4c429	1985222c-0209-4bb0-8c64-e3942a98db36	606791	2025-06-11 19:13:20.693401	t	0	2025-06-11 19:08:20.696408
4cb38565-a122-45ed-a515-7a6061a93bf7	5c1a68ad-ee3c-4b25-b730-7bf5751fda68	711545	2025-06-12 01:24:58.065706	t	1	2025-06-12 01:19:58.081709
54440f08-9b4e-4095-8fb4-19f9386cdfc1	26f39c51-9f7c-4ddc-9197-1660beaa1f4c	737992	2025-06-13 00:21:41.604648	t	0	2025-06-13 00:16:41.653638
cb22ae79-6b2f-41c7-a52c-ecd772ddf59b	7ef7f10c-92a7-4cd6-9b72-c2694fd7df87	957538	2025-06-05 02:35:37.931019	t	1	2025-06-05 02:30:38.007025
935a9bed-efa2-49c8-b441-ff9264aa32aa	33e6a890-bf90-4cee-8fc7-d8f735d74367	918975	2025-06-05 02:56:23.660754	t	0	2025-06-05 02:51:23.665748
563cabcb-010c-48cb-9d15-e1e8d375487e	f87ae792-9e6c-4350-9de8-d5190de253c0	862958	2025-06-13 01:06:33.338082	f	0	2025-06-13 01:01:33.345081
8c00a0bf-08a2-4e96-9d11-2ce442a2125c	3185f727-5eff-4dd0-b139-73eeea666646	116798	2025-06-13 01:09:13.457752	f	0	2025-06-13 01:04:13.464717
917539f4-5f54-4181-bf1a-49094b1f98ab	b42319c0-2d18-495d-b039-4c05eb262def	769385	2025-06-05 16:59:55.758555	t	0	2025-06-05 16:54:55.773896
54158676-3d7d-4507-878d-b64bc26ac18a	6af7e73e-65bd-4c86-b093-df4c473341d5	755816	2025-06-07 15:43:40.528486	t	0	2025-06-07 15:38:40.569483
cd20a6db-d0eb-4d57-83d5-90a23ef98773	9a870b03-4d06-4bdf-8426-7a1f9f975804	728116	2025-06-13 01:11:07.156582	t	1	2025-06-13 01:06:07.160583
8e97265a-9e25-4be6-9a43-22282c6af1d4	cbccfc28-9bc6-4f82-9463-b90a658104e7	765166	2025-06-13 02:42:44.449298	t	0	2025-06-13 02:37:44.464306
94ae9e73-cd10-45fa-8efa-fee696db154d	0fc1f65a-a762-42a6-b97a-94c5b9f4780a	431026	2025-06-13 14:42:22.467152	t	0	2025-06-13 14:37:22.522147
79142f96-14a6-4285-a4ab-cd7aa6aee235	380531f0-2b77-4454-80fc-7df075713750	570214	2025-06-13 18:35:15.954551	t	0	2025-06-13 18:30:16.028024
f72de853-18f7-427d-97e9-d8fad89298c5	fce3e272-9cdc-45a5-abd0-77e25b729932	978781	2025-06-14 16:00:42.690764	t	0	2025-06-14 15:55:42.708761
429e0a75-1dcb-469f-a1f4-0d9674685a77	6a95473a-c736-4539-81c0-67241fabede4	103820	2025-06-15 13:23:36.386578	t	0	2025-06-15 13:18:36.423575
9c2f21ab-9430-4097-86fe-7a49793168c5	c3414899-dee7-4e0a-93ad-c4e467b11679	366966	2025-06-15 18:27:58.960675	t	0	2025-06-15 18:22:59.007092
09439c24-3b64-4d3a-9fd0-53f16cd1b93d	d5362e83-17c6-46d7-9464-3838cf983e42	338218	2025-06-15 19:08:33.355531	t	0	2025-06-15 19:03:33.358543
a742b96f-f80b-4615-bf65-6b1ac3a778ee	a7a2f0f9-237e-4502-a42e-2c6ba322f73c	342720	2025-06-16 03:15:27.088156	t	0	2025-06-16 03:10:27.140968
0de20702-ea1e-4d5c-b70c-5b1b275463b1	65b9ecc9-6908-454b-8965-41c28228ad03	386030	2025-06-16 03:48:35.960291	t	0	2025-06-16 03:43:35.972258
8bd6b99e-82b9-48f6-8b8e-b6c3a35c40f7	31f7c81a-d21e-4b9f-ad1e-67a606d49459	535726	2025-06-20 04:28:41.149387	t	0	2025-06-20 04:23:41.205351
663815da-3d9d-4eb8-a2b9-cd815897c640	726b62fe-3dc5-45da-8490-e0d2f7f06715	318078	2025-06-20 04:42:21.331515	t	0	2025-06-20 04:37:21.334443
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, email, phone, password_hash, role, is_active, email_verified, created_at, updated_at) FROM stdin;
7ef7f10c-92a7-4cd6-9b72-c2694fd7df87	guptaaayu953@gmail.com	5464641321	$2a$12$dPA1oEeMpDYj5aU1OpXjdOPCd14h/OBznpWUqNgdG0l1gbjqA4Uhi	ADMIN	f	t	2025-06-05 02:30:37.894689	2025-06-05 02:31:15.011229
33e6a890-bf90-4cee-8fc7-d8f735d74367	tekeg62268@acedby.com	5464741321	$2a$12$cOuINAiEL4Gpoq/RqN8gh.E3ft9cFeN3eh174Dgm54SQNCw3128dC	PLATFORM_ADMIN	f	t	2025-06-05 02:51:23.658763	2025-06-05 02:52:15.294151
b42319c0-2d18-495d-b039-4c05eb262def	popoy55218@baxima.com	6465454545	$2a$12$5GdDTYgHj0uE3IjXxSE1MOtrHdxMZU376Er5Trgx4aim/V6acMLZi	SERVICE_PROVIDER	f	t	2025-06-05 16:54:55.756043	2025-06-05 16:56:00.609267
6af7e73e-65bd-4c86-b093-df4c473341d5	geworos606@2mik.com	6415454545	$2a$12$iG5CTEjMqoUPAI07hi7zoOF2e4/OX50XcfiS60Mbmya8LmT4o32dK	SERVICE_PROVIDER	f	t	2025-06-07 15:38:40.509478	2025-06-07 15:40:03.234368
ecae01be-1bf7-4397-9bfc-0f427a470a2f	jahekam972@3dboxer.com	45445655445	$2a$12$bLlAcqKH0AlLPVKXO/bggefw6yakxuQeiNXTAOf7r6CuMjib1qi6G	RESIDENT	f	t	2025-06-10 21:58:23.649387	2025-06-10 21:59:57.070615
dabba96f-7002-4cec-9a5d-d40f312d6baf	medede8479@3dboxer.com	6475454545	$2a$12$6bOB.Won/t81eHR6XUftXOIcYqClSQ6qvb4s.Rw.wL7Op.NzdWeHa	SERVICE_PROVIDER	f	t	2025-06-11 01:17:08.926615	2025-06-11 01:17:39.45153
d0b0cbe2-c960-4e1d-8571-eae248ca9e60	bifiyem978@3dboxer.com	1597561234	$2a$12$82fVUmHuyV2DzsL6FGeovuf82GsWdgsYpWSWOzdFD43/Uwqs5J.yC	RESIDENT	f	t	2025-06-11 14:04:45.276752	2025-06-11 14:05:54.39591
4919d3f4-eef6-4c37-b358-7470434d175a	desag37171@2mik.com	789987789	$2a$12$tKf91Y7N2B4IKV50YX2rIO0EbnkauSgFufn94KDY75w.SCHL2E1xO	PLATFORM_ADMIN	f	f	2025-06-11 19:02:13.480988	2025-06-11 19:02:13.482008
1985222c-0209-4bb0-8c64-e3942a98db36	cebat55538@3dboxer.com	78978974	$2a$12$xwvAgKvvZUwGrNt9zojDhOiKC/GcxrrZ2ua/aYIL4uswZKVxRdLAe	PLATFORM_ADMIN	f	t	2025-06-11 19:08:20.692399	2025-06-11 19:08:44.671914
5c1a68ad-ee3c-4b25-b730-7bf5751fda68	vificat103@3dboxer.com	64754354545	$2a$12$mQNjR1yHd9AWqwuK6bqPsuYoNlW03LJon53X43ePxJxA3L515vUZ.	ADMIN	f	t	2025-06-12 01:19:58.061716	2025-06-12 01:20:32.729274
26f39c51-9f7c-4ddc-9197-1660beaa1f4c	kemav77332@2mik.com	789457894	$2a$12$/pZJWSkeVU2NC15cQrmuhuXdW7p3piG4btF749.j7JpXf5IRomL8O	ADMIN	f	t	2025-06-13 00:16:41.570642	2025-06-13 00:17:06.385032
f87ae792-9e6c-4350-9de8-d5190de253c0	vifict103@3dboxer.com	6475354545	$2a$12$nS564iaUHDHCdIVwK5xKie5RT19nYfOXM0NNAnW8s6gt0w0YnpM5O	ADMIN	f	f	2025-06-13 01:01:33.336077	2025-06-13 01:01:33.336077
3185f727-5eff-4dd0-b139-73eeea666646	vifict103@3dtboxer.com	6475354y545	$2a$12$zHibhmwd8xJbeioMkyup7umdNz162O9jVKVLJZW/zLnK7hDkFa.s6	ADMIN	f	f	2025-06-13 01:04:13.455753	2025-06-13 01:04:13.455753
9a870b03-4d06-4bdf-8426-7a1f9f975804	kagigib200@3dboxer.com	564546547	$2a$12$B2BJRuW7zC/dUJgpZUPdYO8FZi0ogMeCS9j964gmCOgsXpRXdlFYq	ADMIN	f	t	2025-06-13 01:06:07.155588	2025-06-13 01:06:36.606695
cbccfc28-9bc6-4f82-9463-b90a658104e7	toniya7624@3dboxer.com	15346543445	$2a$12$rK8/N4e5SUG6CKvEcF4H0OJxMu3Z6.b4x712UBFrzuxgJ52hiwlDu	PLATFORM_ADMIN	f	t	2025-06-13 02:37:44.447572	2025-06-13 02:38:06.951501
0fc1f65a-a762-42a6-b97a-94c5b9f4780a	sorin76821@2mik.com	1456465474	$2a$12$AfExOPgxvs7wuv8cGzMtnuk1VLsBTWPFzigqIhR8dqD/71Z.DYux6	ADMIN	f	t	2025-06-13 14:37:22.3265	2025-06-13 14:37:54.519522
380531f0-2b77-4454-80fc-7df075713750	mavebeg953@cristout.com	1112225554	$2a$12$rDMpZPq1hH37mIBsIGdXX.imBaFIC.0GU8faxjov2oKQo/SAJNb36	ADMIN	f	t	2025-06-13 18:30:15.925849	2025-06-13 18:30:38.526034
fce3e272-9cdc-45a5-abd0-77e25b729932	miremov116@cristout.com	4431654654745	$2a$12$hM1xsa.DxFO9ZgmajKzY/eXurV3R8rj315gi8Vn/8T2cLfCs9eFme	RESIDENT	f	t	2025-06-14 15:55:42.684823	2025-06-14 15:56:16.158974
6a95473a-c736-4539-81c0-67241fabede4	liveya4683@cristout.com	6475547547	$2a$12$PBmbqqqUWfIXvrLWrhlh7uDFHCQuOluLTH1/yExywzrytcTjOfJVO	SERVICE_PROVIDER	f	t	2025-06-15 13:18:36.367578	2025-06-15 13:19:26.458587
c3414899-dee7-4e0a-93ad-c4e467b11679	rejex25861@calorpg.com	461215451254	$2a$12$m/ctDv0uX1rWhnpRD4SYJO/q7ZMQgqVx6Oj5EGMFutZTFA.eHoceC	SERVICE_PROVIDER	f	t	2025-06-15 18:22:58.940503	2025-06-15 18:23:18.824299
d5362e83-17c6-46d7-9464-3838cf983e42	kenabo9076@cristout.com	123456159	$2a$12$RdvtMbhf5hnhpj8EzSjiBuef4dKUUPo8UMS1BHGDMhMMnRczSYNfa	SERVICE_PROVIDER	f	t	2025-06-15 19:03:33.354529	2025-06-15 19:03:57.750339
a7a2f0f9-237e-4502-a42e-2c6ba322f73c	ranono8405@cristout.com	45645645	$2a$12$W2ffwXDCYVyrpQnzQsWU0OKB0e6vECjYfSL6IeCBfIxPc0iNgELBa	ADMIN	f	t	2025-06-16 03:10:27.081164	2025-06-16 03:11:44.877302
65b9ecc9-6908-454b-8965-41c28228ad03	rikavan564@calorpg.com	6464545564	$2a$12$onqZ46elH3pc77YOluyOru.NivTZbnVYF7Kwvtt7YZDeDorwBB3J6	RESIDENT	f	t	2025-06-16 03:43:35.960291	2025-06-16 03:43:54.661397
31f7c81a-d21e-4b9f-ad1e-67a606d49459	yedonic498@nab4.com	15616545445	$2a$12$WPlK5MJ/JcbjO/hmW4BgquzeFE.VlJx33N.ksbbDA7O2So0D3fuL2	ADMIN	f	t	2025-06-20 04:23:41.120812	2025-06-20 04:24:07.443093
726b62fe-3dc5-45da-8490-e0d2f7f06715	vafoj93840@nab4.com	154654656	$2a$12$imdgF3aCL3o3Ruu4Ks9dbOqXRiAssf38e4.sEOmCOQJpvDUuTixN2	RESIDENT	f	t	2025-06-20 04:37:21.330473	2025-06-20 04:37:41.177075
\.


--
-- Name: admin_profiles admin_profiles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admin_profiles
    ADD CONSTRAINT admin_profiles_pkey PRIMARY KEY (user_id);


--
-- Name: admin_profiles admin_profiles_user_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admin_profiles
    ADD CONSTRAINT admin_profiles_user_id_key UNIQUE (user_id);


--
-- Name: platform_admin_profiles platform_admin_profiles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.platform_admin_profiles
    ADD CONSTRAINT platform_admin_profiles_pkey PRIMARY KEY (user_id);


--
-- Name: provider_responses provider_responses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.provider_responses
    ADD CONSTRAINT provider_responses_pkey PRIMARY KEY (id);


--
-- Name: provider_responses provider_responses_request_id_provider_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.provider_responses
    ADD CONSTRAINT provider_responses_request_id_provider_id_key UNIQUE (request_id, provider_id);


--
-- Name: request_media request_media_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.request_media
    ADD CONSTRAINT request_media_pkey PRIMARY KEY (id);


--
-- Name: request_status_history request_status_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.request_status_history
    ADD CONSTRAINT request_status_history_pkey PRIMARY KEY (id);


--
-- Name: resident_profiles resident_profiles_user_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.resident_profiles
    ADD CONSTRAINT resident_profiles_user_id_key UNIQUE (user_id);


--
-- Name: service_categories service_categories_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_categories
    ADD CONSTRAINT service_categories_name_key UNIQUE (name);


--
-- Name: service_categories service_categories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_categories
    ADD CONSTRAINT service_categories_pkey PRIMARY KEY (id);


--
-- Name: service_provider_categories service_provider_categories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_provider_categories
    ADD CONSTRAINT service_provider_categories_pkey PRIMARY KEY (id);


--
-- Name: service_provider_categories service_provider_categories_service_provider_id_category_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_provider_categories
    ADD CONSTRAINT service_provider_categories_service_provider_id_category_id_key UNIQUE (service_provider_id, category_id);


--
-- Name: service_provider_profiles service_provider_profiles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_provider_profiles
    ADD CONSTRAINT service_provider_profiles_pkey PRIMARY KEY (user_id);


--
-- Name: service_provider_societies service_provider_societies_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_provider_societies
    ADD CONSTRAINT service_provider_societies_pkey PRIMARY KEY (id);


--
-- Name: service_provider_societies service_provider_societies_service_provider_id_society_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_provider_societies
    ADD CONSTRAINT service_provider_societies_service_provider_id_society_id_key UNIQUE (service_provider_id, society_id);


--
-- Name: service_ratings service_ratings_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_ratings
    ADD CONSTRAINT service_ratings_pkey PRIMARY KEY (id);


--
-- Name: service_ratings service_ratings_request_id_resident_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_ratings
    ADD CONSTRAINT service_ratings_request_id_resident_id_key UNIQUE (request_id, resident_id);


--
-- Name: service_requests service_requests_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_requests
    ADD CONSTRAINT service_requests_pkey PRIMARY KEY (id);


--
-- Name: service_schedules service_schedules_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_schedules
    ADD CONSTRAINT service_schedules_pkey PRIMARY KEY (id);


--
-- Name: society society_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.society
    ADD CONSTRAINT society_pkey PRIMARY KEY (id);


--
-- Name: user_otps user_otps_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_otps
    ADD CONSTRAINT user_otps_pkey PRIMARY KEY (id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_phone_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_phone_key UNIQUE (phone);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: idx_expires_at; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_expires_at ON public.user_otps USING btree (expires_at);


--
-- Name: idx_request_media_request_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_request_media_request_id ON public.request_media USING btree (request_id);


--
-- Name: idx_request_status_history_created_at; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_request_status_history_created_at ON public.request_status_history USING btree (created_at);


--
-- Name: idx_request_status_history_request_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_request_status_history_request_id ON public.request_status_history USING btree (request_id);


--
-- Name: idx_service_ratings_overall_rating; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_service_ratings_overall_rating ON public.service_ratings USING btree (overall_rating);


--
-- Name: idx_service_ratings_provider_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_service_ratings_provider_id ON public.service_ratings USING btree (provider_id);


--
-- Name: idx_service_requests_created_at; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_service_requests_created_at ON public.service_requests USING btree (created_at);


--
-- Name: idx_service_requests_preferred_date; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_service_requests_preferred_date ON public.service_requests USING btree (preferred_date);


--
-- Name: idx_service_requests_provider_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_service_requests_provider_id ON public.service_requests USING btree (provider_id);


--
-- Name: idx_service_requests_resident_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_service_requests_resident_id ON public.service_requests USING btree (resident_id);


--
-- Name: idx_service_requests_society_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_service_requests_society_id ON public.service_requests USING btree (society_id);


--
-- Name: idx_service_requests_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_service_requests_status ON public.service_requests USING btree (status);


--
-- Name: idx_service_schedules_provider_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_service_schedules_provider_id ON public.service_schedules USING btree (provider_id);


--
-- Name: idx_service_schedules_scheduled_date; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_service_schedules_scheduled_date ON public.service_schedules USING btree (scheduled_date);


--
-- Name: idx_user_otp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_otp ON public.user_otps USING btree (user_id);


--
-- Name: admin_profiles admin_profiles_society_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admin_profiles
    ADD CONSTRAINT admin_profiles_society_id_fkey FOREIGN KEY (society_id) REFERENCES public.society(id);


--
-- Name: admin_profiles admin_profiles_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admin_profiles
    ADD CONSTRAINT admin_profiles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: service_provider_societies fk5nckaepbu9urswm8kioa5xfqm; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_provider_societies
    ADD CONSTRAINT fk5nckaepbu9urswm8kioa5xfqm FOREIGN KEY (approved_by) REFERENCES public.admin_profiles(user_id);


--
-- Name: platform_admin_profiles platform_admin_profiles_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.platform_admin_profiles
    ADD CONSTRAINT platform_admin_profiles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: provider_responses provider_responses_provider_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.provider_responses
    ADD CONSTRAINT provider_responses_provider_id_fkey FOREIGN KEY (provider_id) REFERENCES public.service_provider_profiles(user_id) ON DELETE CASCADE;


--
-- Name: provider_responses provider_responses_request_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.provider_responses
    ADD CONSTRAINT provider_responses_request_id_fkey FOREIGN KEY (request_id) REFERENCES public.service_requests(id) ON DELETE CASCADE;


--
-- Name: request_media request_media_request_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.request_media
    ADD CONSTRAINT request_media_request_id_fkey FOREIGN KEY (request_id) REFERENCES public.service_requests(id) ON DELETE CASCADE;


--
-- Name: request_media request_media_uploaded_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.request_media
    ADD CONSTRAINT request_media_uploaded_by_fkey FOREIGN KEY (uploaded_by) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: request_status_history request_status_history_changed_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.request_status_history
    ADD CONSTRAINT request_status_history_changed_by_fkey FOREIGN KEY (changed_by) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: request_status_history request_status_history_request_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.request_status_history
    ADD CONSTRAINT request_status_history_request_id_fkey FOREIGN KEY (request_id) REFERENCES public.service_requests(id) ON DELETE CASCADE;


--
-- Name: resident_profiles resident_profiles_society_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.resident_profiles
    ADD CONSTRAINT resident_profiles_society_id_fkey FOREIGN KEY (society_id) REFERENCES public.society(id);


--
-- Name: resident_profiles resident_profiles_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.resident_profiles
    ADD CONSTRAINT resident_profiles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: service_provider_categories service_provider_categories_category_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_provider_categories
    ADD CONSTRAINT service_provider_categories_category_id_fkey FOREIGN KEY (category_id) REFERENCES public.service_categories(id) ON DELETE CASCADE;


--
-- Name: service_provider_categories service_provider_categories_service_provider_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_provider_categories
    ADD CONSTRAINT service_provider_categories_service_provider_id_fkey FOREIGN KEY (service_provider_id) REFERENCES public.service_provider_profiles(user_id) ON DELETE CASCADE;


--
-- Name: service_provider_profiles service_provider_profiles_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_provider_profiles
    ADD CONSTRAINT service_provider_profiles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: service_provider_societies service_provider_societies_approved_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_provider_societies
    ADD CONSTRAINT service_provider_societies_approved_by_fkey FOREIGN KEY (approved_by) REFERENCES public.users(id);


--
-- Name: service_provider_societies service_provider_societies_service_provider_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_provider_societies
    ADD CONSTRAINT service_provider_societies_service_provider_id_fkey FOREIGN KEY (service_provider_id) REFERENCES public.service_provider_profiles(user_id) ON DELETE CASCADE;


--
-- Name: service_provider_societies service_provider_societies_society_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_provider_societies
    ADD CONSTRAINT service_provider_societies_society_id_fkey FOREIGN KEY (society_id) REFERENCES public.society(id) ON DELETE CASCADE;


--
-- Name: service_ratings service_ratings_provider_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_ratings
    ADD CONSTRAINT service_ratings_provider_id_fkey FOREIGN KEY (provider_id) REFERENCES public.service_provider_profiles(user_id) ON DELETE CASCADE;


--
-- Name: service_ratings service_ratings_request_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_ratings
    ADD CONSTRAINT service_ratings_request_id_fkey FOREIGN KEY (request_id) REFERENCES public.service_requests(id) ON DELETE CASCADE;


--
-- Name: service_ratings service_ratings_resident_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_ratings
    ADD CONSTRAINT service_ratings_resident_id_fkey FOREIGN KEY (resident_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: service_requests service_requests_category_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_requests
    ADD CONSTRAINT service_requests_category_id_fkey FOREIGN KEY (category_id) REFERENCES public.service_categories(id) ON DELETE RESTRICT;


--
-- Name: service_requests service_requests_provider_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_requests
    ADD CONSTRAINT service_requests_provider_id_fkey FOREIGN KEY (provider_id) REFERENCES public.service_provider_profiles(user_id) ON DELETE SET NULL;


--
-- Name: service_requests service_requests_provider_id_fkey1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_requests
    ADD CONSTRAINT service_requests_provider_id_fkey1 FOREIGN KEY (provider_id) REFERENCES public.service_provider_profiles(user_id);


--
-- Name: service_requests service_requests_resident_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_requests
    ADD CONSTRAINT service_requests_resident_id_fkey FOREIGN KEY (resident_id) REFERENCES public.resident_profiles(user_id) ON DELETE CASCADE;


--
-- Name: service_requests service_requests_resident_id_fkey1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_requests
    ADD CONSTRAINT service_requests_resident_id_fkey1 FOREIGN KEY (resident_id) REFERENCES public.resident_profiles(user_id);


--
-- Name: service_requests service_requests_society_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_requests
    ADD CONSTRAINT service_requests_society_id_fkey FOREIGN KEY (society_id) REFERENCES public.society(id) ON DELETE RESTRICT;


--
-- Name: service_schedules service_schedules_provider_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_schedules
    ADD CONSTRAINT service_schedules_provider_id_fkey FOREIGN KEY (provider_id) REFERENCES public.service_provider_profiles(user_id) ON DELETE CASCADE;


--
-- Name: service_schedules service_schedules_request_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service_schedules
    ADD CONSTRAINT service_schedules_request_id_fkey FOREIGN KEY (request_id) REFERENCES public.service_requests(id) ON DELETE CASCADE;


--
-- Name: society society_approved_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.society
    ADD CONSTRAINT society_approved_by_fkey FOREIGN KEY (approved_by) REFERENCES public.platform_admin_profiles(user_id) ON DELETE SET NULL;


--
-- Name: society society_requested_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.society
    ADD CONSTRAINT society_requested_by_fkey FOREIGN KEY (requested_by) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: user_otps user_otps_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_otps
    ADD CONSTRAINT user_otps_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

