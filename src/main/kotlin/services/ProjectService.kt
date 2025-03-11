package com.northshore.services

import com.northshore.com.northshore.models.TaskPriority
import com.northshore.models.*

class ProjectService {

    fun getProjects(): List<Project> {
        // Create project managers
        val projectManager1 =
            User(1, "John", "Doe", "jdoe", "jdoe@company.com", "password", UserRole.ADMIN, "2025-03-06")
        val projectManager2 = User(2, "Jane", "Smith", "jsmith", "jsmith@company.com", "password", UserRole.PROJECT_MANAGER, "2025-03-06")
        val projectManager3 = User(3, "Michael", "Johnson", "mjohnson", "mjohnson@company.com", "password", UserRole.PROJECT_MANAGER, "2025-03-06")
        val projectManager4 = User(4, "Emily", "Williams", "ewilliams", "ewilliams@company.com", "password", UserRole.PROJECT_MANAGER, "2025-03-06")
        val projectManager5 = User(5, "David", "Brown", "dbrown", "dbrown@company.com", "password", UserRole.ADMIN, "2025-03-06")

// Create 25 projects with tasks
        val projects = listOf(
            Project(
                id = 1L,
                name = "Website Redesign",
                description = "Modernize company website with responsive design",
                startDate = "2025-03-10T00:00:00",
                endDate = "2025-06-30T23:59:59",
                projectManager = projectManager1,
                createdAt = "2025-03-06T12:00:00",
                tasks = mutableListOf(
                    Task(
                        id = 1,
                        projectId = 1,
                        name = "Design mockups",
                        description = "Create visual mockups for homepage and key sections",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = 2,
                        projectId = 1,
                        name = "Frontend implementation",
                        description = "Implement responsive HTML/CSS based on approved designs",
                        priority = TaskPriority.MEDIUM
                    ),
                    Task(
                        id = 3,
                        projectId = 1,
                        name = "Backend integration",
                        description = "Connect frontend to CMS and databases",
                        priority = TaskPriority.HIGH
                    )
                )
            ),

            Project(
                id = 2L,
                name = "Mobile App Development",
                description = "Create iOS and Android versions of customer portal",
                startDate = "2025-04-15T00:00:00",
                endDate = "2025-10-31T23:59:59",
                projectManager = projectManager2,
                createdAt = "2025-03-06T12:30:00",
                tasks = mutableListOf(
                    Task(
                        id = 4,
                        projectId = 2,
                        name = "Requirements gathering",
                        description = "Document functional and non-functional requirements",
                        priority = TaskPriority.CRITICAL
                    ),
                    Task(
                        id = 5,
                        projectId = 2,
                        name = "UI/UX design",
                        description = "Design user interface with focus on usability",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = 6,
                        projectId = 2,
                        name = "iOS development",
                        description = "Develop native iOS application using Swift",
                        priority = TaskPriority.MEDIUM
                    )
                )
            ),

            Project(
                id = 3L,
                name = "Database Migration",
                description = "Migrate legacy database to PostgreSQL",
                startDate = "2025-05-01T00:00:00",
                endDate = "2025-07-31T23:59:59",
                projectManager = projectManager3,
                createdAt = "2025-03-06T13:15:00",
                tasks = mutableListOf(
                    Task(
                        id = 7,
                        projectId = 3,
                        name = "Data mapping",
                        description = "Map legacy data structures to new schema",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = 8,
                        projectId = 3,
                        name = "Migration script development",
                        description = "Create scripts for automated data transfer",
                        priority = TaskPriority.MEDIUM
                    ),
                    Task(
                        id = 9,
                        projectId = 3,
                        name = "Testing and validation",
                        description = "Verify data integrity after migration",
                        priority = TaskPriority.CRITICAL
                    )
                )
            ),

            Project(
                id = 4L,
                name = "API Gateway Implementation",
                description = "Implement centralized API gateway for microservices",
                startDate = "2025-06-15T00:00:00",
                endDate = "2025-09-30T23:59:59",
                projectManager = projectManager4,
                createdAt = "2025-03-06T14:00:00",
                tasks = mutableListOf(
                    Task(
                        id = 10,
                        projectId = 4,
                        name = "Architecture design",
                        description = "Design gateway architecture and routing rules",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = 11,
                        projectId = 4,
                        name = "Authentication integration",
                        description = "Implement OAuth and API key authentication",
                        priority = TaskPriority.CRITICAL
                    ),
                    Task(
                        id = 12,
                        projectId = 4,
                        name = "Rate limiting",
                        description = "Implement rate limiting and throttling",
                        priority = TaskPriority.MEDIUM
                    )
                )
            ),

            Project(
                id = 5L,
                name = "DevOps Pipeline Upgrade",
                description = "Modernize CI/CD pipeline with GitHub Actions",
                startDate = "2025-04-01T00:00:00",
                endDate = "2025-06-15T23:59:59",
                projectManager = projectManager5,
                createdAt = "2025-03-06T14:45:00",
                tasks = mutableListOf(
                    Task(
                        id = 13,
                        projectId = 5,
                        name = "Current pipeline analysis",
                        description = "Document current workflow and identify bottlenecks",
                        priority = TaskPriority.MEDIUM
                    ),
                    Task(
                        id = 14,
                        projectId = 5,
                        name = "GitHub Actions configuration",
                        description = "Create workflow configurations for all projects",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = 15,
                        projectId = 5,
                        name = "Developer training",
                        description = "Train team on new workflow and best practices",
                        priority = TaskPriority.LOW
                    )
                )
            ),

            Project(
                id = 6L,
                name = "Security Audit",
                description = "Comprehensive security audit of all systems",
                startDate = "2025-07-01T00:00:00",
                endDate = "2025-08-31T23:59:59",
                projectManager = projectManager1,
                createdAt = "2025-03-06T15:30:00",
                tasks = mutableListOf(
                    Task(
                        id = 16,
                        projectId = 6,
                        name = "Vulnerability scanning",
                        description = "Scan all systems for known vulnerabilities",
                        priority = TaskPriority.CRITICAL
                    ),
                    Task(
                        id = 17,
                        projectId = 6,
                        name = "Code review",
                        description = "Review critical code paths for security issues",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = 18,
                        projectId = 6,
                        name = "Remediation planning",
                        description = "Develop plan to address identified issues",
                        priority = TaskPriority.HIGH
                    )
                )
            ),

            Project(
                id = 7L,
                name = "Customer Portal Enhancement",
                description = "Add new features to customer self-service portal",
                startDate = "2025-05-15T00:00:00",
                endDate = "2025-08-15T23:59:59",
                projectManager = projectManager2,
                createdAt = "2025-03-06T16:15:00",
                tasks = mutableListOf(
                    Task(
                        id = 19,
                        projectId = 7,
                        name = "Feature specification",
                        description = "Detail requirements for new features",
                        priority = TaskPriority.MEDIUM
                    ),
                    Task(
                        id = 20,
                        projectId = 7,
                        name = "UI development",
                        description = "Implement new interface components",
                        priority = TaskPriority.MEDIUM
                    ),
                    Task(
                        id = 21,
                        projectId = 7,
                        name = "Backend APIs",
                        description = "Develop supporting APIs for new features",
                        priority = TaskPriority.HIGH
                    )
                )
            ),

            Project(
                id = 8L,
                name = "Data Analytics Platform",
                description = "Build internal data analytics platform for business teams",
                startDate = "2025-06-01T00:00:00",
                endDate = "2025-12-31T23:59:59",
                projectManager = projectManager3,
                createdAt = "2025-03-06T17:00:00",
                tasks = mutableListOf(
                    Task(
                        id = 22,
                        projectId = 8,
                        name = "Data warehouse setup",
                        description = "Configure data warehouse and ETL pipelines",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = 23,
                        projectId = 8,
                        name = "Dashboard development",
                        description = "Create interactive dashboards for key metrics",
                        priority = TaskPriority.MEDIUM
                    ),
                    Task(
                        id = 24,
                        projectId = 8,
                        name = "User training",
                        description = "Train business users on platform usage",
                        priority = TaskPriority.LOW
                    )
                )
            ),

            Project(
                id = 9L,
                name = "Legacy System Retirement",
                description = "Decommission legacy ordering system",
                startDate = "2025-08-01T00:00:00",
                endDate = "2025-10-31T23:59:59",
                projectManager = projectManager4,
                createdAt = "2025-03-06T17:45:00",
                tasks = mutableListOf(
                    Task(
                        id = 25,
                        projectId = 9,
                        name = "Data archiving",
                        description = "Archive historical data for compliance",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = 26,
                        projectId = 9,
                        name = "Service transition",
                        description = "Transition services to new platform",
                        priority = TaskPriority.CRITICAL
                    ),
                    Task(
                        id = 27,
                        projectId = 9,
                        name = "Decommissioning plan",
                        description = "Plan for hardware and software decommissioning",
                        priority = TaskPriority.MEDIUM
                    )
                )
            ),

            Project(
                id = 10L,
                name = "Accessibility Compliance",
                description = "Ensure all digital properties meet WCAG 2.1 AA standards",
                startDate = "2025-04-15T00:00:00",
                endDate = "2025-07-15T23:59:59",
                projectManager = projectManager5,
                createdAt = "2025-03-06T18:30:00",
                tasks = mutableListOf(
                    Task(
                        id = 28,
                        projectId = 10,
                        name = "Accessibility audit",
                        description = "Audit all properties for compliance issues",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = 29,
                        projectId = 10,
                        name = "Remediation work",
                        description = "Fix identified accessibility issues",
                        priority = TaskPriority.MEDIUM
                    ),
                    Task(
                        id = 30,
                        projectId = 10,
                        name = "Automated testing",
                        description = "Implement automated accessibility testing",
                        priority = TaskPriority.LOW
                    )
                )
            ),

            Project(
                id = 11L,
                name = "E-commerce Integration",
                description = "Integrate with new payment gateway and shipping providers",
                startDate = "2025-05-01T00:00:00",
                endDate = "2025-07-31T23:59:59",
                projectManager = projectManager1,
                createdAt = "2025-03-06T09:15:00",
                tasks = mutableListOf(
                    Task(
                        id = 31,
                        projectId = 11,
                        name = "Payment gateway integration",
                        description = "Integrate with new payment providers",
                        priority = TaskPriority.CRITICAL
                    ),
                    Task(
                        id = 32,
                        projectId = 11,
                        name = "Shipping API development",
                        description = "Develop interfaces to shipping providers",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = 33,
                        projectId = 11,
                        name = "Testing and certification",
                        description = "Test and certify new integrations",
                        priority = TaskPriority.HIGH
                    )
                )
            ),

            Project(
                id = 12L,
                name = "Cloud Migration",
                description = "Migrate on-premise infrastructure to cloud platform",
                startDate = "2025-06-15T00:00:00",
                endDate = "2026-01-31T23:59:59",
                projectManager = projectManager2,
                createdAt = "2025-03-06T10:30:00",
                tasks = mutableListOf(
                    Task(
                        id = 34,
                        projectId = 12,
                        name = "Architecture planning",
                        description = "Design cloud architecture and networking",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = 35,
                        projectId = 12,
                        name = "Data migration",
                        description = "Migrate databases and storage to cloud",
                        priority = TaskPriority.CRITICAL
                    ),
                    Task(
                        id = 36,
                        projectId = 12,
                        name = "Application migration",
                        description = "Refactor and deploy applications to cloud",
                        priority = TaskPriority.HIGH
                    )
                )
            ),

            Project(
                id = 13L,
                name = "Product Catalog Overhaul",
                description = "Redesign product catalog and categorization system",
                startDate = "2025-07-01T00:00:00",
                endDate = "2025-10-15T23:59:59",
                projectManager = projectManager3,
                createdAt = "2025-03-06T11:45:00",
                tasks = mutableListOf(
                    Task(
                        id = 37,
                        projectId = 13,
                        name = "Taxonomy development",
                        description = "Develop new product categorization system",
                        priority = TaskPriority.MEDIUM
                    ),
                    Task(
                        id = 38,
                        projectId = 13,
                        name = "Data restructuring",
                        description = "Adapt database to new catalog structure",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = 39,
                        projectId = 13,
                        name = "Search improvement",
                        description = "Enhance search functionality with new taxonomy",
                        priority = TaskPriority.MEDIUM
                    )
                )
            ),

            Project(
                id = 14L,
                name = "Internal Tools Modernization",
                description = "Update internal admin tools with modern technology stack",
                startDate = "2025-05-15T00:00:00",
                endDate = "2025-09-30T23:59:59",
                projectManager = projectManager4,
                createdAt = "2025-03-06T12:15:00",
                tasks = mutableListOf(
                    Task(
                        id = 40,
                        projectId = 14,
                        name = "Technology evaluation",
                        description = "Evaluate and select new technology stack",
                        priority = TaskPriority.MEDIUM
                    ),
                    Task(
                        id = 41,
                        projectId = 14,
                        name = "UI redesign",
                        description = "Redesign admin interfaces for improved usability",
                        priority = TaskPriority.LOW
                    ),
                    Task(
                        id = 42,
                        projectId = 14,
                        name = "Backend modernization",
                        description = "Refactor backend services to new architecture",
                        priority = TaskPriority.HIGH
                    )
                )
            ),

            Project(
                id = 15L,
                name = "Reporting System Enhancement",
                description = "Upgrade financial and operational reporting system",
                startDate = "2025-08-01T00:00:00",
                endDate = "2025-11-30T23:59:59",
                projectManager = projectManager5,
                createdAt = "2025-03-06T13:00:00",
                tasks = mutableListOf(
                    Task(
                        id = 43,
                        projectId = 15,
                        name = "Requirements gathering",
                        description = "Collect reporting needs from stakeholders",
                        priority = TaskPriority.MEDIUM
                    ),
                    Task(
                        id = 44,
                        projectId = 15,
                        name = "Report template development",
                        description = "Create new report templates and formats",
                        priority = TaskPriority.LOW
                    ),
                    Task(
                        id = 45,
                        projectId = 15,
                        name = "Data pipeline development",
                        description = "Build data pipelines for automated reporting",
                        priority = TaskPriority.HIGH
                    )
                )
            ),

            Project(
                id = 16L,
                name = "Customer Feedback System",
                description = "Implement new customer feedback collection and analysis system",
                startDate = "2025-04-01T00:00:00",
                endDate = "2025-07-31T23:59:59",
                projectManager = projectManager1,
                createdAt = "2025-03-06T13:45:00",
                tasks = mutableListOf(
                    Task(
                        id = 46,
                        projectId = 16,
                        name = "Survey system implementation",
                        description = "Implement customer survey capabilities",
                        priority = TaskPriority.MEDIUM
                    ),
                    Task(
                        id = 47,
                        projectId = 16,
                        name = "Analytics development",
                        description = "Develop analytics for feedback data",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = 48,
                        projectId = 16,
                        name = "Integration with CRM",
                        description = "Integrate feedback with customer records",
                        priority = TaskPriority.MEDIUM
                    )
                )
            ),

            Project(
                id = 17L,
                name = "Inventory Management System",
                description = "Upgrade inventory tracking and management system",
                startDate = "2025-06-01T00:00:00",
                endDate = "2025-10-31T23:59:59",
                projectManager = projectManager2,
                createdAt = "2025-03-06T14:30:00",
                tasks = mutableListOf(
                    Task(
                        id = 49,
                        projectId = 17,
                        name = "Barcode system upgrade",
                        description = "Upgrade to QR code based tracking",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = 50,
                        projectId = 17,
                        name = "Mobile app development",
                        description = "Develop app for warehouse staff",
                        priority = TaskPriority.MEDIUM
                    ),
                    Task(
                        id = 51,
                        projectId = 17,
                        name = "Reporting enhancement",
                        description = "Improve inventory reporting capabilities",
                        priority = TaskPriority.LOW
                    )
                )
            ),

            Project(
                id = 18L,
                name = "Performance Optimization",
                description = "Optimize system performance for core business applications",
                startDate = "2025-07-15T00:00:00",
                endDate = "2025-09-30T23:59:59",
                projectManager = projectManager3,
                createdAt = "2025-03-06T15:15:00",
                tasks = mutableListOf(
                    Task(
                        id = 52,
                        projectId = 18,
                        name = "Performance audit",
                        description = "Identify performance bottlenecks",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = 53,
                        projectId = 18,
                        name = "Database optimization",
                        description = "Optimize database queries and indexes",
                        priority = TaskPriority.CRITICAL
                    ),
                    Task(
                        id = 54,
                        projectId = 18,
                        name = "Caching implementation",
                        description = "Implement caching strategies",
                        priority = TaskPriority.MEDIUM
                    )
                )
            ),

            Project(
                id = 19L,
                name = "HR System Integration",
                description = "Integrate HR systems with payroll and benefits providers",
                startDate = "2025-05-01T00:00:00",
                endDate = "2025-08-31T23:59:59",
                projectManager = projectManager4,
                createdAt = "2025-03-06T16:00:00",
                tasks = mutableListOf(
                    Task(
                        id = 55,
                        projectId = 19,
                        name = "API development",
                        description = "Develop integration APIs for HR systems",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = 56,
                        projectId = 19,
                        name = "Data synchronization",
                        description = "Implement bidirectional data sync",
                        priority = TaskPriority.MEDIUM
                    ),
                    Task(
                        id = 57,
                        projectId = 19,
                        name = "User acceptance testing",
                        description = "Conduct testing with HR department",
                        priority = TaskPriority.LOW
                    )
                )
            ),

            Project(
                id = 20L,
                name = "Compliance Documentation",
                description = "Update system documentation for compliance audit",
                startDate = "2025-08-15T00:00:00",
                endDate = "2025-10-15T23:59:59",
                projectManager = projectManager5,
                createdAt = "2025-03-06T16:45:00",
                tasks = mutableListOf(
                    Task(
                        id = 58,
                        projectId = 20,
                        name = "Security documentation",
                        description = "Document security controls and procedures",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = 59,
                        projectId = 20,
                        name = "Process documentation",
                        description = "Document business processes for compliance",
                        priority = TaskPriority.MEDIUM
                    ),
                    Task(
                        id = 60,
                        projectId = 20,
                        name = "Audit preparation",
                        description = "Prepare systems and teams for audit review",
                        priority = TaskPriority.CRITICAL
                    )
                )
            ),

            Project(
                id = 21L,
                name = "Mobile App Redesign",
                description = "Redesign mobile app with new brand guidelines",
                startDate = "2025-09-01T00:00:00",
                endDate = "2025-12-15T23:59:59",
                projectManager = projectManager1,
                createdAt = "2025-03-06T08:30:00",
                tasks = mutableListOf(
                    Task(
                        id = 61,
                        projectId = 21,
                        name = "Design system creation",
                        description = "Create mobile design system components",
                        priority = TaskPriority.MEDIUM
                    ),
                    Task(
                        id = 62,
                        projectId = 21,
                        name = "UI implementation",
                        description = "Implement new UI in mobile app",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = 63,
                        projectId = 21,
                        name = "Usability testing",
                        description = "Test new design with focus groups",
                        priority = TaskPriority.LOW
                    )
                )
            ),

            Project(
                id = 22L,
                name = "Data Backup System",
                description = "Implement new enterprise backup and recovery system",
                startDate = "2025-04-15T00:00:00",
                endDate = "2025-07-15T23:59:59",
                projectManager = projectManager2,
                createdAt = "2025-03-06T09:45:00",
                tasks = mutableListOf(
                    Task(
                        id = 64,
                        projectId = 22,
                        name = "Backup strategy planning",
                        description = "Develop comprehensive backup strategy",
                        priority = TaskPriority.CRITICAL
                    ),
                    Task(
                        id = 65,
                        projectId = 22,
                        name = "System implementation",
                        description = "Deploy backup software and configure systems",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = 66,
                        projectId = 22,
                        name = "Disaster recovery testing",
                        description = "Test recovery procedures and timing",
                        priority = TaskPriority.HIGH
                    )
                )
            ),

            Project(
                id = 23L,
                name = "Single Sign-On Implementation",
                description = "Implement SSO across all company applications",
                startDate = "2025-06-01T00:00:00",
                endDate = "2025-09-30T23:59:59",
                projectManager = projectManager3,
                createdAt = "2025-03-06T10:15:00",
                tasks = mutableListOf(
                    Task(
                        id = 67,
                        projectId = 23,
                        name = "Identity provider selection",
                        description = "Evaluate and select identity provider",
                        priority = TaskPriority.MEDIUM
                    ),
                    Task(
                        id = 68,
                        projectId = 23,
                        name = "Application integration",
                        description = "Integrate all applications with SSO system",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = 69,
                        projectId = 23,
                        name = "User migration",
                        description = "Migrate users to new authentication system",
                        priority = TaskPriority.MEDIUM
                    )
                )
            ),

            Project(
                id = 24L,
                name = "Content Management System Upgrade",
                description = "Upgrade CMS to latest version with enhanced features",
                startDate = "2025-07-15T00:00:00",
                endDate = "2025-10-31T23:59:59",
                projectManager = projectManager4,
                createdAt = "2025-03-06T11:00:00",
                tasks = mutableListOf(
                    Task(
                        id = 70,
                        projectId = 24,
                        name = "Version assessment",
                        description = "Assess upgrade requirements and impacts",
                        priority = TaskPriority.MEDIUM
                    ),
                    Task(
                        id = 71,
                        projectId = 24,
                        name = "Test environment setup",
                        description = "Configure test environment for upgrade",
                        priority = TaskPriority.LOW
                    ),
                    Task(
                        id = 72,
                        projectId = 24,
                        name = "Content migration",
                        description = "Migrate content to new system version",
                        priority = TaskPriority.HIGH
                    )
                )
            ),

            Project(
                id = 25L,
                name = "Supply Chain Optimization",
                description = "Optimize supply chain processes and integrations",
                startDate = "2025-08-01T00:00:00",
                endDate = "2026-01-31T23:59:59",
                projectManager = projectManager5,
                createdAt = "2025-03-06T11:30:00",
                tasks = mutableListOf(
                    Task(
                        id = 73,
                        projectId = 25,
                        name = "Process analysis",
                        description = "Analyze current supply chain processes",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = 74,
                        projectId = 25,
                        name = "Vendor integration",
                        description = "Enhance integration with key vendors",
                        priority = TaskPriority.MEDIUM
                    ),
                    Task(
                        id = 75,
                        projectId = 25,
                        name = "Reporting improvements",
                        description = "Improve supply chain visibility and reporting",
                        priority = TaskPriority.LOW
                    )
                )
            )
        )

        return projects
    }

    fun getTeamMembers(): List<User> {
        return listOf(
            User(
                2, firstName = "Jane", lastName = "Smith", username = "jsmith", email = "jsmith@company.com",
                password = "password", role = UserRole.PROJECT_MANAGER, createdAt = "2025-03-06"
            ),
            User(
                7, firstName = "Robert", lastName = "Wilson", username = "rwilson", email = "rwilson@company.com",
                password = "password", role = UserRole.PROJECT_MANAGER, createdAt = "2025-03-06"
            ),
            User(
                10, firstName = "Lisa", lastName = "Garcia", username = "lgarcia", email = "lgarcia@company.com",
                password = "password", role = UserRole.ADMIN, createdAt = "2025-03-06"
            ),
            User(
                3, firstName = "Michael", lastName = "Johnson", username = "mjohnson", email = "mjohnson@company.com",
                password = "password", role = UserRole.DEVELOPER, createdAt = "2025-03-06"
            )
        )
    }

    fun getListOfUsers(): List<User> {
        return listOf(
            User(
                1, firstName = "John", lastName = "Doe", username = "jdoe", email = "jdoe@company.com",
                password = "password", role = UserRole.ADMIN, createdAt = "2025-03-06"
            ),

            User(
                2, firstName = "Jane", lastName = "Smith", username = "jsmith", email = "jsmith@company.com",
                password = "password", role = UserRole.PROJECT_MANAGER, createdAt = "2025-03-06"
            ),

            User(
                3, firstName = "Michael", lastName = "Johnson", username = "mjohnson", email = "mjohnson@company.com",
                password = "password", role = UserRole.DEVELOPER, createdAt = "2025-03-06"
            ),

            User(
                4, firstName = "Emily", lastName = "Williams", username = "ewilliams", email = "ewilliams@company.com",
                password = "password", role = UserRole.TESTER, createdAt = "2025-03-06"
            ),

            User(
                5, firstName = "David", lastName = "Brown", username = "dbrown", email = "dbrown@company.com",
                password = "password", role = UserRole.ANALYST, createdAt = "2025-03-06"
            ),

            User(
                6, firstName = "Sarah", lastName = "Miller", username = "smiller", email = "smiller@company.com",
                password = "password", role = UserRole.DEVELOPER, createdAt = "2025-03-06"
            ),

            User(
                7, firstName = "Robert", lastName = "Wilson", username = "rwilson", email = "rwilson@company.com",
                password = "password", role = UserRole.PROJECT_MANAGER, createdAt = "2025-03-06"
            ),

            User(
                8, firstName = "Jessica", lastName = "Taylor", username = "jtaylor", email = "jtaylor@company.com",
                password = "password", role = UserRole.TESTER, createdAt = "2025-03-06"
            ),

            User(
                9, firstName = "Thomas", lastName = "Anderson", username = "tanderson", email = "tanderson@company.com",
                password = "password", role = UserRole.DEVELOPER, createdAt = "2025-03-06"
            ),

            User(
                10, firstName = "Lisa", lastName = "Garcia", username = "lgarcia", email = "lgarcia@company.com",
                password = "password", role = UserRole.ADMIN, createdAt = "2025-03-06"
            )
        )
    }

    fun getCurrentUser(): User {
        return User(
            10, firstName = "Lisa", lastName = "Garcia", username = "lgarcia", email = "lgarcia@company.com",
            password = "password", role = UserRole.ADMIN, createdAt = "2025-03-06"
        )
    }

    fun getDashboardStats(): DashboardStats {
        return DashboardStats(
            totalRevenue = 53_009.89,
            revenueIncrease = 12.0,
            projectCount = 95,
            projectTotal = 100,
            projectDecrease = 10.0,
            timeSpent = 1022,
            timeTotal = 1300,
            timeIncrease = 8.0,
            resourceCount = 101,
            resourceTotal = 120,
            resourceIncrease = 2.0,
            completedProjects = 26,
            delayedProjects = 35,
            ongoingProjects = 35,
            completionPercentage = 72
        )
    }

}