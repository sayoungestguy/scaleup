-- Insert provided sample data
INSERT INTO code_tables (id, category, code_key, code_value, created_by, created_date, last_modified_by, last_modified_date)
VALUES
(1, 'skill_type', 'teach', 'Teach', 'admin', NOW(), 'admin', NOW()),
(2, 'skill_type', 'learn', 'Learn', 'admin', NOW(), 'admin', NOW()),
(3, 'invite_status', 'pending', 'Pending', 'admin', NOW(), 'admin', NOW()),
(4, 'invite_status', 'accepted', 'Accepted', 'admin', NOW(), 'admin', NOW()),
(5, 'invite_status', 'declined', 'Declined', 'admin', NOW(), 'admin', NOW());


-- Insert sample data with the first entry as admin
INSERT INTO user_profile (id, nickname, job_role, about_me, profile_picture, social_links, created_by, created_date, last_modified_by, last_modified_date, user_id)
VALUES
(1, 'admin', 'Administrator', 'System administrator account.', 'admin_profile.jpg', 'https://linkedin.com/admin', 'admin', NOW(), 'admin', NOW(), 1),
(2, 'John Doe', 'Software Engineer', 'Passionate about coding and open-source.', 'profile1.jpg', 'https://linkedin.com/john_doe', 'user', NOW(), 'user', NOW(), 2);

-- Insert sample data for 20 technology and IT-related skills
INSERT INTO skill (id, skill_name, created_by, created_date, last_modified_by, last_modified_date)
VALUES
(1, 'Python Programming', 'admin', NOW(), 'admin', NOW()),
(2, 'Data Analysis', 'admin', NOW(), 'admin', NOW()),
(3, 'Cloud Computing', 'admin', NOW(), 'admin', NOW()),
(4, 'Machine Learning', 'admin', NOW(), 'admin', NOW()),
(5, 'Cybersecurity', 'admin', NOW(), 'admin', NOW()),
(6, 'DevOps', 'admin', NOW(), 'admin', NOW()),
(7, 'Blockchain Development', 'admin', NOW(), 'admin', NOW()),
(8, 'Database Management', 'admin', NOW(), 'admin', NOW()),
(9, 'Artificial Intelligence', 'admin', NOW(), 'admin', NOW()),
(10, 'JavaScript', 'admin', NOW(), 'admin', NOW()),
(11, 'Web Development', 'admin', NOW(), 'admin', NOW()),
(12, 'Mobile App Development', 'admin', NOW(), 'admin', NOW()),
(13, 'Networking', 'admin', NOW(), 'admin', NOW()),
(14, 'Linux Administration', 'admin', NOW(), 'admin', NOW()),
(15, 'Virtualization', 'admin', NOW(), 'admin', NOW()),
(16, 'Terraform', 'admin', NOW(), 'admin', NOW()),
(17, 'IT Support', 'admin', NOW(), 'admin', NOW()),
(18, 'Software Testing', 'admin', NOW(), 'admin', NOW()),
(19, 'Agile Project Management', 'admin', NOW(), 'admin', NOW()),
(20, 'UI/UX Design', 'admin', NOW(), 'admin', NOW());

-- View the inserted data
SELECT * FROM skills;
-- View the inserted data
SELECT * FROM code_tables;
-- View the inserted data
SELECT * FROM user_profiles;





